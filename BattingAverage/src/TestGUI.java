/**
 * TestGUI by Dagar Ahmed & Kris McCoy   v2.04
 */
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.lang.reflect.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.border.*;
import javax.swing.filechooser.FileFilter;
import javax.tools.*;

@SuppressWarnings({"unchecked", "deprecation"}) 
public class TestGUI 
{
    //ADJUSTABLES.  EDIT AS YOU SEE FIT
    private static final int WIDTH = 940; 
    private static final int HEIGHT = 800; 
    private static final int SOURCE_CODE_FRAME_WIDTH = 940;
    private static final int SOURCE_CODE_FRAME_HEIGHT = 800;
    private static final Font HEADER_FONT = new Font(Font.MONOSPACED, Font.BOLD, 20);
    private static final Font REGULAR_FONT = new Font(Font.MONOSPACED, Font.PLAIN, 16);  
    private static final Font MESSAGE_FONT = new Font(Font.SANS_SERIF, Font.BOLD, 16);  
    private static final Font PATH_FONT = new Font(Font.MONOSPACED, Font.PLAIN, 16);
    private static final Color HEADER_BG_COLOR = new Color(200,200,255); 
    private static final Color MESSAGE_BG_COLOR = new Color(200,200,200); 
    public static final Color RED = new Color(250, 180, 180);      //Output did NOT match expected
    public static final Color GREEN = new Color(160, 225, 200);    //Output DID match expected
    private static final int SCROLL_SPEED = 40;
    private static final String NOFILES_ALERT = "WARNING: The current source directory does not contain any helpful source files.  Choose new source path.";

    //BUT DON'T MESS WITH THESE.
    private static String windowName;
    private static Class testClass;
    private static ArrayList<TestData> testResults = new ArrayList<TestGUI.TestData>();
    private ArrayList<JPanel> subFrameList;     
    private static final PrintStream originalSystemOut = System.out;
    private static final InputStream originalSystemIn = System.in;
    private static InputStream hijackedSystemIn;

    private JFrame mainFrame;
    private JScrollPane scrPane;
    private JTextArea srcLabel;
    private JPanel buttonPanel;
    private JButton loadButton, retestButton;
    private ArrayList<JButton> srcButtonList = new ArrayList<JButton>();

    private static final String mistakeStartFlag = (char)16+"!!", mistakeStopFlag = "!!"+(char)17;

    private static File srcPath;
    private static File originalSrcPath;
    private static JFileChooser chooser;
    private static URL classUrl;
    private static URLClassLoader classLoader;
    private static boolean timeOutThrown = false;

    /**
     * Used on initial build to collect window name and class name containing test cases
     * Default source path is the directory containing this TestGUI.class
     */
    public TestGUI(){    
        try {                        
            String callingClassName = Thread.currentThread().getStackTrace()[2].getClassName();
            this.windowName = callingClassName;
            this.testClass = Class.forName(callingClassName);
        } catch (Exception e) {}     
        letsDoSomeGUI();
    }

    public void letsDoSomeGUI() {
        SwingUtilities.invokeLater(new Runnable() 
            {
                @Override
                public void run() {
                    buildWindow();
                }

            }); 
    }

    public void buildWindow() {
        mainFrame = new JFrame("Current Test Sequence: " + windowName);
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.setSize(WIDTH, HEIGHT);

        BorderLayout buttonLayout = new BorderLayout();
        buttonLayout.setHgap(5);
        buttonPanel = new JPanel(buttonLayout);       
        buttonPanel.setBorder(new EmptyBorder(5,5,5,5));

        loadButton = new JButton("Choose Source");
        initializeFileChooser();
        retestButton = new JButton("Retest");
        retestButton.setEnabled(false);

        loadButton.addActionListener( new ActionListener() 
            { 
                public void actionPerformed(ActionEvent e) { 
                    if (loadPath()) { compileAndTest(); }
                } 
            }
        );

        retestButton.addActionListener(new ActionListener() 
            { 
                public void actionPerformed(ActionEvent e) { 
                    compileAndTest();  
                } 
            }
        );

        setSrcLabel(srcPath);
        buttonPanel.add(srcLabel, BorderLayout.CENTER);    
        buttonPanel.add(loadButton, BorderLayout.WEST);
        buttonPanel.add(retestButton, BorderLayout.EAST);        
        mainFrame.add(buttonPanel, BorderLayout.NORTH);          
        compileAndTest(); //run a test from current directory
    }

    @SuppressWarnings("unchecked")
    private void compileAndTest() {
        if (scrPane != null) { 
            mainFrame.remove(scrPane); 
        }
        retestButton.setEnabled(false);
        loadButton.setEnabled(false);
        buttonPanel.remove(srcLabel);                        
        setSrcLabel("Loading... Please wait.");
        buttonPanel.add(srcLabel, BorderLayout.CENTER);
        mainFrame.repaint();
        mainFrame.setVisible(true);    

        SwingUtilities.invokeLater(new Runnable() 
            {
                @Override
                public void run() {
                    try {
                        testResults = new ArrayList<TestGUI.TestData>(); 
                        compileAllSrcFiles();  
                    } catch(Exception e) { System.out.println("Unable to compile source files."); }
                    try {
                        EchoingByteArrayInputStream.hijackSystemIn();
                        testClass.getMethod("runTests").invoke(testClass);  
                        EchoingByteArrayInputStream.restoreSystemIn();
                    } catch(Exception e) { System.out.println("Error with runTests.  Bad test cases?"); }
                    try {
                        showResults();
                        buttonPanel.remove(srcLabel);                        
                        setSrcLabel(srcPath);
                        buttonPanel.add(srcLabel, BorderLayout.CENTER);         
                        if (!timeOutThrown) {
                            retestButton.setEnabled(true);
                            loadButton.setEnabled(true);
                        }
                        mainFrame.setVisible(true);
                    } catch(Exception e) { System.out.println("Unable to build GUI."); }
                }
            }
        );
    }

    private void initializeFileChooser(){
        // Let the user pick from a filtered list of files
        chooser = new JFileChooser(".");
        Action details = chooser.getActionMap().get("viewTypeDetails");
        details.actionPerformed(null);
        if (srcPath == null) {
            srcPath = originalSrcPath = new File(System.getProperty("user.dir"));
        }

        chooser.setCurrentDirectory(srcPath);
        if (! srcPath.getName().equals(originalSrcPath.getName())) chooser.changeToParentDirectory();
        chooser.setFileSelectionMode( JFileChooser.FILES_AND_DIRECTORIES);
        chooser.setFileFilter(new FileFilter() 
            {
                String description = "Java files";
                @Override
                public boolean accept(File f) {
                    if (f.isDirectory()) 
                        return true;
                    if (f.getName().endsWith(".java"))
                        return true;
                    return false;
                }

                @Override
                public String getDescription() { return this.description; }
            }
        );
    }

    private boolean loadPath() {
        int returnVal = chooser.showOpenDialog(mainFrame);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            File newFile = chooser.getSelectedFile();
            srcPath = (newFile.isDirectory()) ? newFile : newFile.getParentFile();
        } else {
            return false;
        }
        return true;
    }

    public void compileAllSrcFiles() {
        TestData.setClassLoader();
        File[] arrayOfFiles = srcPath.listFiles(new java.io.FileFilter() 
                {
                    @Override
                    public boolean accept(File f) {
                        String fn = f.getName();
                        String exclude1 = testClass.getName()+".java";                        
                        String exclude2 = "TestGUI.java";

                        if (f.getName().endsWith(".java")) {
                            if (f.getName().equals(exclude1) || f.getName().equals(exclude2)) return false;
                            return true;
                        }
                        return false;
                    }
                }
            );
        if (arrayOfFiles.length==0) {
            TestData.messageAlert(NOFILES_ALERT);
            return;
        }

        //Before we compile this list of .java files, let's delete old .class files
        for (File jFile : arrayOfFiles)
        {
            String jFilename = jFile.getPath();
            String cFilename = jFilename.substring(0, jFilename.indexOf(".java")) + ".class";
            File toDelete = new File(cFilename);
            toDelete.delete();
        }

        JavaCompiler javaCompiler = ToolProvider.getSystemJavaCompiler();
        String[] baseArgs = new String[]{
                "-Xlint:none",
                "-d",
                srcPath.getAbsolutePath(),
                "-g",
                "-sourcepath",
                srcPath.getAbsolutePath()
            };

        ArrayList<String> arguments = new ArrayList<String>(Arrays.asList(baseArgs));

        ArrayList<String> fileNames = new ArrayList<String>();
        for (File f : arrayOfFiles) arguments.add(f.getPath());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        javaCompiler.run(null, null, baos, arguments.toArray(new String[arguments.size()]));

        //Data in the baos means there was a compile error.  Try one file at a time.
        if (baos.size() > 0) {
            baos.reset();
            System.out.println("Trying to compile all project files at once caused an error.");
            System.out.println("Please wait while I compile one file at a time.");

            arguments = new ArrayList<String>(Arrays.asList(baseArgs));
            for (File f : arrayOfFiles) {
                arguments.add(f.getPath());
                javaCompiler.run(null, null, baos, arguments.toArray(new String[arguments.size()]));
                arguments.remove(arguments.size()-1);
            }
            if (baos.size() > 0) System.out.println("\n\nCompiler Errors:\n" + baos);
        }

    }

    public void showResults() {   
        subFrameList = new ArrayList<JPanel>();

        for(TestData td : testResults){
            if(td.getHeader() != null)
                makeHeaderFrame(td);
            if(td.getMessage() != null)
                makeMessageFrame(td);
            if(td.getSrcFiles() != null)
                makeSrcButtonFrame(td);
            if ( td.getExpectedOut() != null || td.getActualOut() != null )
                makeFourSubFrames(td);            
            else if(td.getMethodCall() != null)
                makeTwoSubFrames(td);           
        }

        //put all subFrames into a scrollable box
        ScrollablePanel subFrameBox = new ScrollablePanel();
        subFrameBox.setLayout(new BoxLayout(subFrameBox, BoxLayout.Y_AXIS));
        for(JPanel subFrame : subFrameList)
            subFrameBox.add(subFrame);

        //Put that scrollable box into the scroll pane
        if (scrPane != null) { mainFrame.remove(scrPane); }
        scrPane = new JScrollPane(subFrameBox);
        scrPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // add to the scroll pane to the main frame and view it
        mainFrame.add(scrPane, BorderLayout.CENTER);        
        //mainFrame.setVisible(true); 
        SwingUtilities.invokeLater(new Runnable() 
            {
                @Override
                public void run() {
                    scrPane.getViewport().setViewPosition( new Point(0, 0) );
                }
            }
        );
    }    

    public void setSrcLabel(File s){ 
        String p = s.getPath();
        if (p.length()>60) p = "..."+p.substring(p.length()-60);                 
        setSrcLabel("SRC>>"+p);                
    }

    public void setSrcLabel(String s){ 
        srcLabel = new JTextArea(s);
        srcLabel.setFont(PATH_FONT);
        srcLabel.setBackground(Color.WHITE);
        srcLabel.setLineWrap(true);
        srcLabel.setWrapStyleWord(false);
        srcLabel.setEditable(false);
        if (srcPath != null) srcLabel.setToolTipText(srcPath.getPath());
    }

    public void makeHeaderFrame(TestData td){
        JPanel jp = new JPanel();
        subFrameList.add(jp);
        JTextArea textArea = new JTextArea("\n"+td.getHeader()+"\n");
        textArea.setFont(HEADER_FONT);
        textArea.setBackground(HEADER_BG_COLOR);        
        textArea.setColumns(td.getHeader().length());
        textArea.setLineWrap(false);
        textArea.setEditable(false);
        jp.setBackground(HEADER_BG_COLOR);
        jp.setBorder(BorderFactory.createLineBorder(new Color(0)));
        jp.add(textArea,BorderLayout.CENTER);        
    }

    public void makeMessageFrame(TestData td){
        JPanel jp = new JPanel(new BorderLayout());        
        subFrameList.add(jp); 

        JPanel jpInner = new JPanel(new BorderLayout());
        jpInner.setBorder(new EmptyBorder(5,15,5,5));
        jpInner.setBackground(td.getResultColor());

        JTextArea textArea = new JTextArea(td.getMessage());
        textArea.setFont(MESSAGE_FONT);
        textArea.setBackground(td.getResultColor());
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        jp.setBackground(td.getResultColor());
        jp.setBorder(BorderFactory.createLineBorder(new Color(0)));
        jpInner.add(textArea,BorderLayout.CENTER);
        jp.add(jpInner,BorderLayout.CENTER);
    }

    public void makeSrcButtonFrame(TestData td){
        JPanel jp = new JPanel(new BorderLayout());        
        subFrameList.add(jp); 

        JPanel jpInner = new JPanel(new FlowLayout(FlowLayout.LEADING));
        jpInner.setBorder(new EmptyBorder(0,15,0,5));
        jpInner.setBackground(Color.WHITE);
        JTextArea lbl = new JTextArea("Source Files: ");
        lbl.setBackground(Color.WHITE);
        jpInner.add(lbl);

        String[] fileName = td.getSrcFiles();
        int oldSize = srcButtonList.size();        
        for(int i = 0; i < fileName.length; i++) {   
            //Make button

            //See if file exists.  Disable button if file is missing.
            File temp = null;
            try { 
                temp = new File(srcPath + "\\" + fileName[i]);
                if (! temp.exists()) {
                    temp = new File(srcPath + "\\" + fileName[i] + ".java");
                }
                if (! temp.exists()) temp = null;
            } 
            catch (Exception e) { }            

            JButton newButton;
            if (temp != null) {
                newButton = new JButton(temp.getName());
            } else {
                newButton = new JButton(fileName[i]);
                newButton.setEnabled(false);
            }
            srcButtonList.add(newButton);              
        }

        for(int i = oldSize; i < srcButtonList.size(); i++){
            final int index = i;
            srcButtonList.get(index).addActionListener( new ActionListener() 
                { 
                    public void actionPerformed(ActionEvent e) { 
                        SourceCodeFrame srcFrame = new SourceCodeFrame(srcPath + "\\" + srcButtonList.get(index).getLabel());
                    } 
                }
            );
            jpInner.add(srcButtonList.get(index));
        }

        JScrollPane srcScrollable = new JScrollPane(jpInner,
                JScrollPane.VERTICAL_SCROLLBAR_NEVER,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        WheelScrolling.install(srcScrollable);
        srcScrollable.setBorder(BorderFactory.createEmptyBorder());

        jp.add(srcScrollable,BorderLayout.CENTER);
        jp.setBorder(BorderFactory.createLineBorder(new Color(0)));
    }

    public JPanel getMethodCallPanel(TestData td) {
        JPanel jp = new JPanel(new BorderLayout());   
        jp.setBackground(Color.WHITE);
        jp.setBorder(BorderFactory.createTitledBorder("Invoking"));

        JPanel jpInner = new JPanel(new BorderLayout());
        jpInner.setBorder(new EmptyBorder(5,5,5,5));
        jpInner.setBackground(Color.WHITE);

        JTextArea textArea = new JTextArea(td.getMethodCall());        
        textArea.setFont(REGULAR_FONT);
        textArea.setLineWrap(true);
        textArea.setEditable(false);
        jpInner.add(textArea,BorderLayout.CENTER);
        jp.add(jpInner,BorderLayout.CENTER);
        return jp;
    }

    public JPanel getResultPanel(TestData td) {
        JPanel jp = new JPanel(new BorderLayout());
        jp.setBackground(Color.WHITE);
        jp.setBorder(BorderFactory.createTitledBorder("Result"));

        JPanel jpInner = new JPanel(new BorderLayout());
        jpInner.setBorder(new EmptyBorder(5,5,5,5));
        jpInner.setBackground(Color.WHITE);

        JTextArea textArea = new JTextArea(td.getResult());        
        textArea.setFont(REGULAR_FONT);        
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        if (td.getResultColor() != null) {
            textArea.setBackground(td.getResultColor());
            textArea.setFont(textArea.getFont().deriveFont(Font.BOLD));
        }
        jpInner.add(textArea,BorderLayout.CENTER);
        jp.add(jpInner,BorderLayout.CENTER);
        return jp;
    }

    public JPanel getExpOutPanel(TestData td) {
        JPanel jp = new JPanel(new BorderLayout());;    
        jp.setBackground(Color.WHITE);
        jp.setBorder(BorderFactory.createTitledBorder("Expected Output/Return Value"));

        JPanel jpInner = new JPanel(new BorderLayout());
        jpInner.setBorder(new EmptyBorder(5,5,5,5));
        jpInner.setBackground(Color.WHITE);

        StyledDocument document = new DefaultStyledDocument();
        SimpleAttributeSet attributes = new SimpleAttributeSet();
        StyleConstants.setFontFamily(attributes, "Monospace");
        attributes.addAttribute(StyleConstants.FontConstants.Family, Font.MONOSPACED);
        attributes.addAttribute(StyleConstants.FontConstants.Size, 16);
        try {
            if (td.getExpectedOut() != null)
                document.insertString(0, td.getExpectedOut() + "\n", attributes);            
        } catch (BadLocationException badLocationException) {
            System.out.println("Unable to build expected output document.");
        }

        /*  I CAN'T FIGURE OUT HOW TO SET THIS
        textArea.setLineWrap(true);
         */
        JTextPane textPane = new JTextPane(document);
        textPane.setEditable(false);
        jpInner.add(textPane,BorderLayout.CENTER);
        jp.add(jpInner,BorderLayout.CENTER);
        return jp;
    }

    public JPanel getActOutPanel(TestData td) {
        JPanel jp = new JPanel(new BorderLayout());
        jp.setBackground(Color.WHITE);
        jp.setBorder(BorderFactory.createTitledBorder("Actual Output/Return Value"));

        JPanel jpInner = new JPanel(new BorderLayout());
        jpInner.setBorder(new EmptyBorder(5,5,5,5));
        jpInner.setBackground(Color.WHITE);

        StyledDocument document = new DefaultStyledDocument();
        SimpleAttributeSet attributesNormal = new SimpleAttributeSet();
        StyleConstants.setFontFamily(attributesNormal, "Monospace");
        attributesNormal.addAttribute(StyleConstants.FontConstants.Family, Font.MONOSPACED);
        attributesNormal.addAttribute(StyleConstants.FontConstants.Size, 16);
        SimpleAttributeSet attributesMistake = (SimpleAttributeSet)(attributesNormal.clone());
        attributesMistake.addAttribute(StyleConstants.CharacterConstants.Bold, Boolean.TRUE);
        attributesMistake.addAttribute(StyleConstants.ColorConstants.Foreground, new Color(200, 0, 0));

        String textToInsert = td.getActualOut();
        if (textToInsert != null)
            try {
                int startLocation = textToInsert.indexOf(mistakeStartFlag);
                int stopLocation = textToInsert.indexOf(mistakeStopFlag);
                while (startLocation != -1 && stopLocation != -1) {
                    String good = textToInsert.substring(0, startLocation);
                    String mistake = textToInsert.substring(startLocation+mistakeStartFlag.length(), stopLocation);
                    textToInsert = textToInsert.substring(stopLocation+mistakeStopFlag.length())  ;                 
                    document.insertString(document.getLength(), good, attributesNormal);
                    document.insertString(document.getLength(), mistake, attributesMistake);                
                    startLocation = textToInsert.indexOf(mistakeStartFlag);
                    stopLocation = textToInsert.indexOf(mistakeStopFlag);
                }
                //insert remaining text            
                document.insertString(document.getLength(), textToInsert + "\n", attributesNormal);
            } catch (BadLocationException badLocationException) {
                System.out.println("Unable to parse actual output for style document.");
            }

        JTextPane textPane = new JTextPane(document);
        textPane.setEditable(false);
        jpInner.add(textPane,BorderLayout.CENTER);
        jp.add(jpInner,BorderLayout.CENTER);
        return jp;
    }

    public void makeFourSubFrames(TestData td){
        GridLayout myGL = new GridLayout(1,2);
        myGL.setHgap(8);
        EmptyBorder padding = new EmptyBorder(5,10,5,10);

        JPanel jpOuter = new JPanel();        
        jpOuter.setLayout(new BoxLayout(jpOuter, BoxLayout.Y_AXIS));
        JPanel jpTop = new JPanel(myGL);        
        JPanel jpBottom = new JPanel(myGL);  

        jpTop.setBackground(Color.WHITE);
        jpBottom.setBackground(Color.WHITE);
        jpTop.setBorder(padding);        
        jpBottom.setBorder(padding);

        //4 panels
        JPanel topLeft = new JPanel(new BorderLayout());
        topLeft.add(getMethodCallPanel(td), BorderLayout.NORTH);
        topLeft.setBackground(Color.WHITE);

        JPanel topRight = new JPanel(new BorderLayout());
        topRight.setBackground(Color.WHITE);
        topRight.add(getResultPanel(td), BorderLayout.NORTH);

        JScrollPane bottomLeftScrollable = new JScrollPane(getExpOutPanel(td),
                JScrollPane.VERTICAL_SCROLLBAR_NEVER,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        WheelScrolling.install(bottomLeftScrollable);
        bottomLeftScrollable.setBorder(BorderFactory.createEmptyBorder());

        JScrollPane bottomRightScrollable = new JScrollPane(getActOutPanel(td),
                JScrollPane.VERTICAL_SCROLLBAR_NEVER,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        WheelScrolling.install(bottomRightScrollable);
        bottomRightScrollable.setBorder(BorderFactory.createEmptyBorder());

        jpTop.add(topLeft);
        jpTop.add(topRight);              
        jpBottom.add(bottomLeftScrollable);  
        jpBottom.add(bottomRightScrollable);

        jpOuter.add(jpTop);
        jpOuter.add(jpBottom);
        jpOuter.setBackground(Color.WHITE);
        jpOuter.setBorder(BorderFactory.createLineBorder(new Color(0))); 
        subFrameList.add(jpOuter);
    }

    public void makeTwoSubFrames(TestData td){
        GridLayout myGL = new GridLayout(1,2);
        myGL.setHgap(8);
        EmptyBorder padding = new EmptyBorder(5,10,5,10);

        JPanel jpOuter = new JPanel();        
        jpOuter.setLayout(new BoxLayout(jpOuter, BoxLayout.Y_AXIS));
        JPanel jpTop = new JPanel(myGL);     

        jpTop.setBackground(Color.WHITE);
        jpTop.setBorder(padding);   

        //2 panels
        JPanel topLeft = new JPanel(new BorderLayout());
        topLeft.add(getMethodCallPanel(td), BorderLayout.NORTH);
        topLeft.setBackground(Color.WHITE);

        JPanel topRight = new JPanel(new BorderLayout());
        topRight.setBackground(Color.WHITE);
        topRight.add(getResultPanel(td), BorderLayout.NORTH);

        jpTop.add(topLeft);
        jpTop.add(topRight);          

        jpOuter.add(jpTop);
        jpOuter.setBackground(Color.WHITE);
        jpOuter.setBorder(BorderFactory.createLineBorder(new Color(0))); 
        subFrameList.add(jpOuter);
    }

    /**
     * ScrollablePanel is basically a JPanel that will properly resize when it contains a JScrollPane
     */
    private static class ScrollablePanel extends JPanel implements Scrollable {        
        public Dimension getPreferredScrollableViewportSize() { return super.getPreferredSize(); }

        public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) { return SCROLL_SPEED; }

        public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) { return SCROLL_SPEED; }

        public boolean getScrollableTracksViewportWidth() { return true; }

        public boolean getScrollableTracksViewportHeight() { return false; }
    } //end Inner Class ScrollablePanel

    /**
     * A TestData object performs requested tests and stores the information collected during the test.  
     * All these TestData objects are loaded into a list of TestResults 
     * which is used to build the GUI's results scroll pane.
     */
    static class TestData {
        private static ByteArrayOutputStream baos = new ByteArrayOutputStream();  
        private static long timeOutSec = 2;
        private String methodCall, result;
        private String expectedOut, actualOut;   
        private Color resultColor;
        private String header, message;        
        private String[] srcFiles;

        public TestData() {  } 

        public TestData(String s, boolean isHeader) {
            if (isHeader)  this.header = s;
            else  this.message = s;
        }

        public TestData(String methodCall, String result, String expectedOut, String actualOut) {
            this(methodCall, result, expectedOut, actualOut, new Color(255, 255, 255));
        }

        public TestData(String methodCall, String result, String expectedOut, String actualOut, Color resultColor) {
            //header and message remain null
            this.methodCall = methodCall;
            this.result = result;
            this.expectedOut = expectedOut;
            this.actualOut = actualOut;
            this.resultColor = resultColor;
        }

        public TestData(String[] srcFiles){
            this.srcFiles = srcFiles;
        }

        public static void setTimeOutSec(int sec) { timeOutSec = (long) sec; }

        public void setColor(int r, int g, int b, int a) { resultColor = new Color(r, g, b, a); }

        public void setColor(int r, int g, int b) { resultColor = new Color(r, g, b); }

        public String getHeader() { return header; }

        public String getMessage() { return message; }

        public String getMethodCall() { return methodCall; }

        public String getResult() { return result; }

        public String getExpectedOut() { return expectedOut; }

        public String getActualOut() { return actualOut; }

        public Color getResultColor() { return resultColor; }

        public String[] getSrcFiles(){
            return srcFiles;
        }

        public String setHeader(String header) { 
            this.header = header;
            return header;
        }

        public String setMessage(String message) { 
            resultColor = MESSAGE_BG_COLOR;
            return this.message = message; 
        }

        public void setResultColorForMessage(boolean correct) { 
            if (correct) resultColor = GREEN;
            else resultColor = RED;
        }

        public String setMethodCall(String methodCall) { return this.methodCall = methodCall; }

        public String setResult(String result) { return this.result = result; }

        public String setResult(String result, Color resultColor) { 
            this.resultColor = resultColor;
            return this.result = result;
        }

        public String setExpectedOut(String expectedOut) { 
            this.expectedOut = expectedOut;
            return expectedOut;
        }

        public String setActualOut(String actualOut) { return this.actualOut = actualOut; }

        public static void header(String name) { 
            testResults.add(new TestGUI.TestData(name, true)); 
        }

        public static void srcButton(String srcFileName) { 
            String[] fileNames = srcFileName.split(",");
            for (int i = 0; i < fileNames.length; i++) fileNames[i] = fileNames[i].trim();
            testResults.add(new TestGUI.TestData(fileNames)); 
        }

        public static void srcButton(String[] srcFileNames) { 
            testResults.add(new TestGUI.TestData(srcFileNames)); 
        }

        private static void messageAlert(String m) {
            TestGUI.TestData newEntry = new TestGUI.TestData(m, false); //set message
            newEntry.setResultColorForMessage(false);  //red 
            testResults.add(0, newEntry);
        }

        public static void message(String m) {
            testResults.add(new TestGUI.TestData(m, false));
        }       

        public static void message(String m, boolean correct) {
            TestGUI.TestData newEntry = new TestGUI.TestData(m, false); //set message
            newEntry.setResultColorForMessage(correct);
            testResults.add(newEntry);
        }  

        public static Class getClassFromName(String cName) {
            try {
                Class<?> c = Class.forName(cName, true, classLoader);
                return c;
            } catch (Exception e) { return null; }
        }

        public static Object makeObject(String cName, Object[] args, String userInputScript) {
            //makeObject instantiates & return an object of class cName with supplied arguments
            baos = new ByteArrayOutputStream(); 
            System.setOut(new PrintStream(baos));
            TestGUI.TestData td = new TestGUI.TestData();
            testResults.add(td);
            if (args == null) args = new Object[0];
            String methodCall = "constructor: " + cName + "(" + getParam(args) + ")";
            if (userInputScript != null) methodCall += "\n**Input provided by user input script";
            td.setMethodCall(methodCall); 
            Object retObject = null;                                  

            ExecutorService es = null;
            FutureTask<?>  theTask = null;

            try {            
                ArrayList<Class[]> argOptions = getWideningOptions(args);
                Class<?> c = Class.forName(cName, true, classLoader);

                Constructor[] availableConstructors = c.getConstructors();
                Constructor conSearch = null; 
                outerLoop:
                for (Class[] argCombo : argOptions) {
                    for (Constructor conOption : availableConstructors) {
                        //See if this conOption is a good fit
                        //It's parameters must match with this argCombo
                        if (conOption.getParameterCount() != argCombo.length) continue;
                        Class[] paramCombo = conOption.getParameterTypes();
                        boolean goodFit = true;
                        for (int i = 0; i < paramCombo.length; i++) {                       
                            if (! (paramCombo[i].isAssignableFrom(argCombo[i])))                            
                                goodFit = false;
                        }
                        if (goodFit) {
                            conSearch = conOption;
                            break outerLoop; //will break for loop as soon as a proper method signature is found
                        }
                    }
                }
                if (conSearch == null) throw new NoSuchMethodException();
                Constructor con = conSearch;                              

                final Object[] argList = args;
                theTask = new FutureTask<Object>(new Callable<Object>() 
                    {
                        public Object call() {
                            Object retObject = null;
                            if (userInputScript != null) 
                                EchoingByteArrayInputStream.injectScript(userInputScript);
                            try {                                    
                                retObject = con.newInstance(argList);
                            } catch (Exception e) { retObject = e; }                                
                            return retObject;
                        }
                    });

                es = Executors.newSingleThreadExecutor();
                es.execute(theTask);
                retObject = theTask.get(timeOutSec, TimeUnit.SECONDS);
                es.shutdownNow();
                if (retObject instanceof Exception) {
                    Exception e = (Exception)retObject;
                    retObject = null;
                    throw (Exception)(e.getCause()); 
                }
                if (retObject != null) td.setResult("Completed.");  
                else td.setResult("Unsuccessful. null object.  Must explicitly declare class as public.", TestGUI.RED);  

                if (baos.size() > 0) td.setActualOut(baos.toString());                   

            } catch (ClassNotFoundException e) {
                td.setResult("Error: Class not found. Ensure that source file exists and will compile.", TestGUI.RED); 
                if (baos.size()>0) td.setActualOut(baos.toString());  
            } catch (NoSuchMethodException e) {
                td.setResult("Error: Could not call constructor", TestGUI.RED); 
                if (baos.size()>0) td.setActualOut(baos.toString()); 
            } catch (TimeoutException e) {             
                td.setResult("Error: Constructor timed out", TestGUI.RED); 
                if (theTask != null) theTask.cancel(true);
                if (es != null) es.shutdownNow();
                //Now let's be more aggressive                
                try{
                    final Field threadField = theTask.getClass().getDeclaredField("runner");
                    threadField.setAccessible(true);                    
                    Thread t = (Thread)threadField.get(theTask);
                    t.stop();
                }catch(Exception e2){
                    System.out.print("Problem occured while forcibly stopping FutureTask thread");
                }
                if (baos.size() > 1000) {
                    byte[] truncBA = baos.toByteArray();
                    String trunc = new String(truncBA, 0, 1000);
                    td.setActualOut(trunc+"...\n[truncated]");
                }
                else
                if (baos.size()>0) td.setActualOut(baos.toString());               
            } catch (NoSuchElementException e) {
                td.setResult("Error: More data was requested than the script provided.", TestGUI.RED); 
                if (baos.size() > 1000) {
                    byte[] truncBA = baos.toByteArray();
                    String trunc = new String(truncBA, 0, 1000);
                    td.setActualOut(trunc+"...\n[truncated]");
                }
                else
                if (baos.size()>0) td.setActualOut(baos.toString());  
            } catch (Exception e) {
                td.setResult("Error: Crashed while running", TestGUI.RED);  
                if (baos.size() > 1000) {
                    byte[] truncBA = baos.toByteArray();
                    String trunc = new String(truncBA, 0, 100);
                    td.setActualOut(trunc+"...\n[truncated]");
                }
                else
                if (baos.size()>0) td.setActualOut(baos.toString());                  
            }

            System.setOut(originalSystemOut); //Ensure that System.out is restored.
            return retObject;                   
        }

        public static Object makeObject(String cName, Object[] args) {
            return makeObject(cName, args, null);  //no input script requested
        }

        public static Object testMethod(Object o, String name, Object[] args, String expOut, String userInputScript) {                
            baos = new ByteArrayOutputStream(); 
            System.setOut(new PrintStream(baos));

            TestGUI.TestData td = new TestGUI.TestData();
            testResults.add(td);
            String methodCall = "";
            if (args == null) args = new Object[0];
            if (o == null) methodCall += "null.";
            else if (o instanceof Class) methodCall += ((Class)o).getName()+".";
            else methodCall += o.getClass().getName().toLowerCase()+"Object.";
            methodCall += name + "(" + getParam(args) + ")";
            if (userInputScript != null) methodCall += "\n**Input provided by user input script";
            td.setMethodCall(methodCall);
            td.setExpectedOut(expOut);
            Object retObject = null;

            ExecutorService es = null;
            FutureTask<?>  theTask = null;

            try { 
                ArrayList<Class[]> argOptions = getWideningOptions(args);

                //Is o an actual object or is it a Class?
                Class c = null;                
                if (o instanceof Class) //If o is a Class, cast it as such.
                    c = (Class)o;
                else {  //o is an object.  We need to know what class it belogs to.
                    if (o != null)
                        c = o.getClass();
                    else {  //o isn't even a thing
                        throw new IllegalArgumentException();
                    }
                }

                Method[] availableMethods = c.getMethods();
                Method methodSearch = null;  
                outerLoop:
                for (Class[] argCombo : argOptions) {
                    for (Method methodOption : availableMethods) {
                        //See if this methodOption is a good fit
                        //Must have same name and it's parameters must match with this argCombo
                        int testNum1 = methodOption.getParameterCount();
                        int testNum2 = argCombo.length;
                        if (!methodOption.getName().equals(name) || methodOption.getParameterCount() != argCombo.length) continue;
                        Class[] paramCombo = methodOption.getParameterTypes();
                        boolean goodFit = true;
                        for (int i = 0; i < paramCombo.length; i++) {                       
                            if (! (paramCombo[i].isAssignableFrom(argCombo[i])))                            
                                goodFit = false;
                        }
                        if (goodFit) {
                            methodSearch = methodOption;
                            break outerLoop; //will break for loop as soon as a proper method signature is found
                        }
                    }
                }
                if (methodSearch == null) throw new NoSuchMethodException();
                Method m = methodSearch;
                final Object[] argList = args;
                theTask = new FutureTask<Object>(new Callable<Object>() 
                    {
                        public Object call() {
                            Object retObject = null;
                            if (userInputScript != null) 
                                EchoingByteArrayInputStream.injectScript(userInputScript);
                            try {
                                retObject = m.invoke(o, argList);
                            } catch (Exception e) { 
                                retObject = e; 
                            }
                            return retObject;
                        }
                    });
                es = Executors.newSingleThreadExecutor();
                es.execute(theTask);
                retObject = theTask.get(timeOutSec, TimeUnit.SECONDS);

                //We got our return object. Politely request the test task to shut down.
                theTask.cancel(true);
                es.shutdownNow();
                if (retObject instanceof Exception) {
                    Exception e = (Exception)retObject;
                    retObject = null;
                    throw (Exception)(e.getCause()); 
                }

                String actOut = null;
                if (retObject != null) { 
                    if (retObject.getClass().isArray()) actOut = baos.toString() + arrayAsString(retObject);
                    else actOut = baos.toString() + retObject;
                } else {  //no return object?  Must have been a void method.  Check baos for printed output.
                    if (baos.size() > 0) actOut = baos.toString();                    
                }      

                td.setActualOut(actOut);  //will be replaced later with marked up output if needed.
                if (expOut != null) {
                    verifyAndMarkUpOutput(td, expOut, actOut);
                } else {
                    td.setResult("Completed.");
                }

            } catch (IllegalArgumentException e) {
                td.setResult("Error: No object exists to run method.", TestGUI.RED);                    
            } catch (NoSuchMethodException e) {            
                td.setResult("Error: Could not find method. Similar signature not found or method not public.", TestGUI.RED); 
            } catch (TimeoutException e) {
                td.setResult("Error: Method timed out (Infinite loop?)", TestGUI.RED); 
                //This is how we used to politely ask for shutdown.
                if (theTask != null) theTask.cancel(true);
                if (es != null) es.shutdownNow();
                //Now let's be more aggressive                
                try{
                    final Field threadField = theTask.getClass().getDeclaredField("runner");
                    threadField.setAccessible(true);                    
                    Thread t = (Thread)threadField.get(theTask);
                    t.stop();
                }catch(Exception e2){
                    System.out.print("Problem occured while forcibly stopping FutureTask thread");
                }
                if (baos.size() > 1000) {
                    byte[] truncBA = baos.toByteArray();
                    String trunc = new String(truncBA, 0, 1000);
                    td.setActualOut(trunc+"...\n[truncated]");
                }
                else
                if (baos.size()>0) td.setActualOut(baos.toString());           
            } catch (NoSuchElementException e) {
                td.setResult("Error: More data was requested than the script provided.", TestGUI.RED); 
                if (baos.size() > 1000) {
                    byte[] truncBA = baos.toByteArray();
                    String trunc = new String(truncBA, 0, 1000);
                    td.setActualOut(trunc+"...\n[truncated]");
                }
                else
                if (baos.size()>0) td.setActualOut(baos.toString());  
            } catch (Exception e) {
                td.setResult("Error: Crashed while running", TestGUI.RED); 
                if (baos.size() > 1000) {
                    byte[] truncBA = baos.toByteArray();
                    String trunc = new String(truncBA, 0, 1000);
                    td.setActualOut(trunc+"...\n[truncated]");
                }
                else
                if (baos.size()>0) td.setActualOut(baos.toString());  
            }

            System.setOut(originalSystemOut); //Ensure that System.out is restored.
            return retObject;             
        }  

        public static Object testMethod(String cName, String name, Object[] args, String expOut, String userInputScript) {              
            //USE STRING FOR CLASS NAME WHEN WANTING TO TEST A STATIC METHOD
            //testMethod (String of class name containing static method, method name, array of method parameters, expected output)      
            //testMethod will print the data returned by calling the method.        
            try {                
                Class<?> c = Class.forName(cName, true, classLoader);
                return testMethod(c, name, args, expOut, userInputScript);
            } catch (ClassNotFoundException e) {
                testResults.add(new TestGUI.TestData(cName+"."+name+"()", 
                        "Error: Class not found. Ensure that source file exists and will compile.", null, null, TestGUI.RED)); 
            } catch (Exception e) {
                testResults.add(new TestGUI.TestData(cName+"."+name+"()", 
                        "Error: No object/class exists to run the method.", null, null, TestGUI.RED));               
            }  
            return null;  //in the event that we had exception
        }        

        public static Object testMethod(Object o, String name, Object[] args, String expOut) {
            return testMethod(o, name, args, expOut, null);
        }

        public static Object testMethod(String cName, String name, Object[] args, String expOut) {              
            return testMethod(cName, name, args, expOut, null);
        }

        public static Object testMethod(Object o, String name, Object[] args) {
            return testMethod(o, name, args, null, null);
        }

        public static Object testMethod(String cName, String name, Object[] args) {              
            return testMethod(cName, name, args, null, null);
        }

        private static void verifyAndMarkUpOutput(TestData td, String expOut, String actOut) {
            if (expOut == null) return;
            if (expOut.equals("") && (actOut == null || actOut.equals(""))) {
                td.setResult("Correct", TestGUI.GREEN);
                return;
            }
            if (actOut == null) {  //we have an expected output, but no actual output
                td.setResult("NO MATCH", TestGUI.RED);
                return;
            }

            //We have an expected output and an actual output.  Let's try to match them up.           
            boolean perfect = true;
            String markup = "";            
            Scanner eo = new Scanner(expOut);
            Scanner ao = new Scanner(actOut);
            int start = -1;  //start location of difference.

            while (perfect && eo.hasNext() && ao.hasNext()) {  //no mistake found and still data to scan
                String eWord = eo.next();
                String aWord = ao.next();

                if (! eWord.equals(aWord))                               
                    try {
                        perfect = false;
                        final Field f = ao.getClass().getDeclaredField("position");
                        f.setAccessible(true);
                        start = (Integer)f.get(ao) - aWord.length();
                    } catch (Exception e) { System.out.print("Problem accessing position within Scanner"); }
            }
            if (!perfect) {  // we found a spot that doesn't match.
                String before, remaining, mistake, after;
                before = actOut.substring(0, start);
                remaining = actOut.substring(start);

                //Find end of the word that we want to flag
                int space = remaining.indexOf(" ");
                int slashn = remaining.indexOf("\n");
                if (space == -1) space = remaining.length();
                if (slashn == -1) slashn = remaining.length();                                
                int end = Math.min(space, slashn);

                mistake = remaining.substring(0, end);
                after = remaining.substring(end);
                markup = before + mistakeStartFlag + mistake + mistakeStopFlag + after;                 
            }            
            if (perfect && eo.hasNext()) {  //no mistake was found, but actual out ran out of data
                perfect = false;
                markup = actOut + "\n" + mistakeStartFlag + "Remaining output is missing." + mistakeStopFlag;                
            }
            if (perfect && ao.hasNext()) {  //no mistake was found, but actual out has extra data
                perfect = false;
                try {
                    perfect = false;
                    final Field f = ao.getClass().getDeclaredField("position");
                    f.setAccessible(true);
                    start = (Integer)f.get(ao);
                } catch (Exception e) { System.out.print("Problem accessing position within Scanner"); }
                markup = actOut.substring(0, start) + mistakeStartFlag + actOut.substring(start) + mistakeStopFlag;                
            }
            if (perfect) {  //tokens matched, but is there a spacing issue?
                if (trimSpacing(expOut).equals(trimSpacing(actOut)))
                    td.setResult("Correct", TestGUI.GREEN);
                else {
                    td.setResult("NO MATCH (Spacing Issue)", TestGUI.RED);
                }                
            } else {
                td.setResult("NO MATCH", TestGUI.RED);
                td.setActualOut(markup);
            }
        }

        private static String trimSpacing(String s) {            
            //student output may differ from expected output by spacing, so let's clean it up.
            if (s == null || s.length() == 0) return s;
            while (s.indexOf("\r\n") > -1)
                s = s.replace("\r\n", "\n");
            while (s.indexOf("\n\n") > -1)
                s = s.replace("\n\n", "\n");                
            while (s.indexOf(" \n") > -1)
                s = s.replace(" \n", "\n");            
            boolean changed;
            do {
                changed = false;            
                if (s.substring(s.length()-1).equals("\n")) {            
                    s=s.substring(0, s.length()-1);
                    changed = true;
                }
                while (s.length()>0 && s.charAt(s.length()-1)==' ') {
                    s=s.substring(0, s.length()-1);
                    changed = true;
                }
            } while (changed && s.length()>0);
            return s;
        }

        private static ArrayList<Class[]> getWideningOptions(Object args[]) {
            //Given an array of arguments (as Objects) determine all possible widening options
            //For example, if the arguments passed were (Float, Long) we would first consider wrappers then
            //the primitive forms, but using the order of windening upcasts we would eventuall consider
            //something like (double, float) as a viable consideration (since float can be unwrapped to a 
            //primitive and upcase to a double and Long can be unwrapped to a primitive and upcast as a float).

            //The complete returned list for (Float, Long) would be:
            //Float     Long
            //float     Long
            //double    Long
            //Float     long
            //float     long
            //double    long
            //Float     float
            //float     float
            //double    float
            //Float     double
            //float     double
            //double    double       

            ArrayList<Class[]> options = new ArrayList<Class[]>();
            ArrayList<Class[]> paths = new ArrayList<Class[]>();

            int argCount = args.length; 
            Class[] originalClassArray = new Class[argCount];
            for (int i = 0; i < argCount; i++) {
                originalClassArray[i] = args[i].getClass();
            }

            int totalCombos = 1;
            for (Class start : originalClassArray) {    
                Class[] path = upcastPath(start);
                totalCombos *= path.length;
                paths.add(path);
            }

            for (int i = 0; i < totalCombos; i++)
                options.add(new Class[argCount]);

            int prevBreakFreq = totalCombos;
            for (int a = argCount-1; a >= 0; a--) {    
                int pathLength = paths.get(a).length;
                int breakFreq = prevBreakFreq/pathLength;
                for (int i = 0; i < totalCombos; i++){  
                    if (a == 1 && i == 6) 
                        pathLength = pathLength;
                    (options.get(i))[a]=(paths.get(a))[i/breakFreq%pathLength];
                }          
                prevBreakFreq = breakFreq;            
            }
            return options;
        }

        private static Class[] getSuperclassPath(Class current) {
            String starting = current.getName();
            java.util.List<Class> list = new ArrayList<Class>(); 
            list.add(current);
            while (current != Object.class) {
                current = current.getSuperclass();
                list.add(current);
            } 

            Class[] cPath = new Class[list.size()];
            for (int i = 0; i < list.size(); i++)
                cPath[i] = list.get(i);

            String path = "" + list;

            int x = 3;          
            return cPath;
        }

        private static Class[] upcastPath(Class original) {
            if (original == Byte.class) return new Class[] {original, byte.class, short.class, int.class, long.class, float.class, double.class, Number.class, Object.class};
            if (original == Short.class) return new Class[] {original, short.class, int.class, long.class, float.class, double.class, Number.class, Object.class};
            if (original == Character.class) return new Class[] {original, char.class, int.class, long.class, float.class, double.class, Number.class, Object.class};
            if (original == Integer.class) return new Class[] {original, int.class, long.class, float.class, double.class, Number.class, Object.class};
            if (original == Long.class) return new Class[] {original, long.class, float.class, double.class, Number.class, Object.class};
            if (original == Float.class) return new Class[] {original, float.class, double.class, Number.class, Object.class};
            if (original == Double.class) return new Class[] {original, double.class, Number.class, Object.class};                                    
            if (original == Boolean.class) return new Class[] {original, boolean.class, Object.class};                                    
            return getSuperclassPath(original);
        }

        private static String arrayAsString(Object o) {
            //Object is an array.  Since we don't want a string representation with just reference addresses
            //we must determining what kind of data is in the array, cast and convert to String
            if (o == null) return "null";
            if (! o.getClass().isArray()) return "";
            if (o instanceof byte[])     return Arrays.toString((byte[])o);
            if (o instanceof short[])    return Arrays.toString((short[])o);
            if (o instanceof int[])      return Arrays.toString((int[])o);
            if (o instanceof long[])     return Arrays.toString((long[])o);
            if (o instanceof float[])    return Arrays.toString((float[])o);
            if (o instanceof double[])   return Arrays.toString((double[])o);
            if (o instanceof boolean[])  return Arrays.toString((boolean[])o);
            if (o instanceof char[])     return Arrays.toString((char[])o);      
            String ret = "[";
            for (Object part : (Object[])o) {
                if (ret.length() > 1) ret += ", ";
                ret += getObjectAsString(part);
            }
            return ret + "]";
        }

        private static String getObjectAsString(Object o) {
            if (o == null) return "null";
            if (o instanceof String) return "\"" + o + "\"";
            if (o instanceof Character) return "\'" + o + "\'";
            if (o instanceof Boolean) return ""+o;
            if (o.getClass().isArray()) return arrayAsString(o);
            if (isNumeric(o.toString())) return ""+o;
            String className = o.getClass().getName();
            if (className.lastIndexOf(".") > -1) className = className.substring(className.lastIndexOf(".")+1);
            return className.toLowerCase()+"Object" ;          
        }

        public static boolean isNumeric(String str) {
            return str != null && str.matches("[-+]?\\d*\\.?\\d+");
        }

        private static String getParam(Object[] params) {
            //returns a string representation of the array of objects
            String ret = "";
            for (Object o : params) {
                if (ret.length() > 0) ret += ", ";
                if (o == null) ret += null;
                else ret += getObjectAsString(o);
            }
            return ret;         
        }     

        private static void setClassLoader() {
            try {
                classUrl = srcPath.toURI().toURL();
                classLoader = URLClassLoader.newInstance(new URL[]{classUrl});
            } catch (Exception e) { };
        }
    }//end inner class TestData

/**
 * EchoingByteArrayInputStream
 * When a student is collecting data from System.In, SysIn will be hijacked to read data from 
 * this  "script" instead.  Since we are faking "user-typed input" this stream echoes the 
 * "user input" to System.out as it is collected by the Scanner so that it looks like it was typed
 * into each prompt by a real human.
 * 
 * 
 * @author Dagar Ahmed & Kris McCoy
 */
static class EchoingByteArrayInputStream extends ByteArrayInputStream {  
    public static void hijackSystemIn() {      
        ArrayList<EchoingByteArrayInputStream> streams = new ArrayList<EchoingByteArrayInputStream>();
        hijackedSystemIn = new SequenceInputStream(Collections.enumeration(streams));
        System.setIn(hijackedSystemIn); 
    }

    public static void injectScript(String script) {
        ArrayList<EchoingByteArrayInputStream> streams = new ArrayList<EchoingByteArrayInputStream>();
        Scanner chopper = new Scanner(script);
        while (chopper.hasNextLine()) {
            streams.add(new EchoingByteArrayInputStream((chopper.nextLine()+'\n').getBytes()));            
        }

        //Inject our collection of streams into the currently hijacked System In
        try{           
            final Field f = hijackedSystemIn.getClass().getDeclaredField("e");
            f.setAccessible(true);
            f.set(hijackedSystemIn, Collections.enumeration(streams));

            final Field field = hijackedSystemIn.getClass().getDeclaredField("in");
            field.setAccessible(true);
            field.set(hijackedSystemIn, (InputStream)Collections.enumeration(streams).nextElement());
        }catch(Exception e){
            System.out.print("Unable to inject input script into System In" + e);
        }
    }

    public static void restoreSystemIn() {
        System.setIn(TestGUI.originalSystemIn);
    }

    public EchoingByteArrayInputStream(byte[] b) { super(b); }

    @Override
    public int read(byte[] b, int off, int len) {
        int data = super.read(b, off, len);
        for (int i = 0; i < data; i++) {
            System.out.print((char)b[i]);
            if (b[i] == 10) // '\n'
                return i+1;
        }
        return data;            
    }

    @Override
    public int read(byte[] b) throws IOException {
        int data = super.read(b);
        for (int i = 0; i < data; i++) {
            System.out.print((char)b[i]);
            if (b[i] == 10) // '\n'
                return i+1;
        }
        return data;
    }
} //end inner class EchoingByteArrayInputStream

public static final class WheelScrolling {
    /**
     * Passes mouse wheel events to the parent component if parent component is scrollable.
     */
    public static void install(JScrollPane pane) {
        pane.addMouseWheelListener(new Listener(pane));
    }

    private static class Listener implements MouseWheelListener {
        private final JScrollPane pane;
        private boolean inHandler; // To avoid StackOverflowError in nested calls

        Listener(JScrollPane pane) {
            this.pane = pane;
            pane.setWheelScrollingEnabled(false);
        }

        public void mouseWheelMoved(MouseWheelEvent e) {
            if (!inHandler) {
                inHandler = true;
                try {
                    handleMoved(e);
                } finally {
                    inHandler = false;
                }
            }
        }

        private void handleMoved(MouseWheelEvent e) {
            JScrollPane curr = currentPane(e);
            JScrollPane parent = getTopmostParentPane(e);

            if (curr == null || parent == null) {
                dispatchDefault(pane, e);
            } else {
                dispatchDefault(parent, (MouseWheelEvent)
                    SwingUtilities.convertMouseEvent(pane, e, curr));
            }
        }

        private static void dispatchDefault(JScrollPane comp, MouseWheelEvent e) {
            if (comp.isWheelScrollingEnabled()) {
                comp.dispatchEvent(e);
            } else {
                comp.setWheelScrollingEnabled(true);
                comp.dispatchEvent(e);
                comp.setWheelScrollingEnabled(false);
            }
        }

        private JScrollPane currentPane(MouseWheelEvent e) {
            Current current = current(pane);
            if (current == null) {
                return null;
            }

            long validUntil = current.validUntil;
            current.validUntil = e.getWhen() + 1000;

            if (e.getWhen() < validUntil) {
                return current.pane;
            }

            for (Component comp = pane; comp != null; comp = comp.getParent()) {
                if (comp instanceof JScrollPane) {
                    JScrollPane otherPane = (JScrollPane) comp;
                    if (canScrollFurther(otherPane, e)) {
                        current.pane = otherPane;
                        return current.pane;
                    }
                }
            }

            current.pane = null;
            return null;
        }

        private JScrollPane getTopmostParentPane(MouseWheelEvent e) {
            Current current = current(pane);
            if (current == null) {
                return null;
            }

            boolean parentSet = false;
            for (Component comp = pane; comp != null; comp = comp.getParent()) {
                if (comp instanceof JScrollPane) {
                    JScrollPane otherPane = (JScrollPane) comp;
                    parentSet = true;
                    current.pane = otherPane;
                }
            }

            if (parentSet)
                return current.pane;

            current.pane = null;
            return null;
        }        

        private static boolean canScrollFurther(JScrollPane pane, MouseWheelEvent e) {

            // See BasicScrollPaneUI
            JScrollBar bar = pane.getVerticalScrollBar();
            if (bar == null || !bar.isVisible() || e.isShiftDown()) {
                bar = pane.getHorizontalScrollBar();
                if (bar == null || !bar.isVisible()) {
                    return false;
                }
            }

            if (e.getWheelRotation() < 0) {
                return bar.getValue() != 0;
            } else {
                int limit = bar.getMaximum() - bar.getVisibleAmount();
                return bar.getValue() != limit;
            }
        }

        private static Current current(Component component) {
            if (component.getParent() == null) {
                return null;
            }

            Component top = component;
            while (top.getParent() != null) {
                top = top.getParent();
            }

            for (MouseWheelListener listener : top.getMouseWheelListeners()) {
                if (listener instanceof Current) {
                    return (Current) listener;
                }
            }

            Current current = new Current();
            top.addMouseWheelListener(current);
            return current;
        }
    }//end inner inner class (WheelScrolling/Listener)

    /**
     * The "currently active scroll pane" needs to remembered once
     * per top-level window.
     * <p>
     * Since a Component does not provide a storage for arbitrary data,
     * this data is stored in a no-op listener.
     */
    private static class Current implements MouseWheelListener {
        private JScrollPane pane;
        private long validUntil;

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            // Do nothing.
        }
    }//end inner inner class (WheelScrolling/Current)
} //end inner class WheelScrolling

/**
 * SourceCodeFrame
 * 
 * Makes a seperate frame when "srcButton" is clicked.
 * Shows all the source code for that .java file
 * you can either include the .java or not its up to the user
 * If src file in a diffrent folder we use the srcSource variable to get the path
 * 
 * @author Dagar Ahmed & Kris McCoy
 */
public class SourceCodeFrame {
    private JFrame frame;

    public SourceCodeFrame(String fileName){makeSourceCodeFrame(fileName);}

    public void makeSourceCodeFrame(String fileName) {
        frame = new JFrame("Source code: " + fileName);
        JFileChooser fc = new JFileChooser();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JTextArea textArea = new JTextArea(10, 10);
        textArea.setFont(REGULAR_FONT);
        textArea.setLineWrap(false);
        textArea.setEditable(false);

        File file = new File(fileName);
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(
                        new FileInputStream(file)));
            textArea.read(input, "READING FILE :-)");
        } catch (Exception e) {
            e.printStackTrace();
        }

        textArea.setEditable(false);
        frame.getContentPane().add(new JScrollPane(textArea), BorderLayout.CENTER);
        frame.pack();
        frame.setSize(SOURCE_CODE_FRAME_WIDTH, SOURCE_CODE_FRAME_HEIGHT);
        frame.setVisible(true);
    }
}//end inner inner class (SourceCodeFrame)

}