import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.text.MaskFormatter;

/**
 * A class that validates user input, used as part of an input form
 */
public class ValidateForm
{
	public ValidateForm() {
		//no variables to initialize
	}

	public boolean isAllAlpha(String str) {
		for (int i = 0; i < str.length(); i++) {
			if (!Character.isLetter(str.charAt(i)))
				return false;
		}
		return true;
	}

	public boolean isNumeric(String str) {
		boolean hasPeriod = false;
		for (int i = 0; i < str.length(); i++) {
			if (!Character.isDigit(str.charAt(i))) {
				if (str.charAt(i) == '.') {
					if (hasPeriod) {
						return false;
					}
					hasPeriod = true;
				} else {
					return false;
				}
			}
		}
		return true;
	}


	/** validate the user's entered name */
	public String checkName(String name) {
		if (name.length() >= 2 && isAllAlpha(name))
			return "";
		return "Invalid name!\n";
	}

	/** validate the user's entered email */
	public String checkEmail(String email) {
		int chars = 0;
		boolean hasAt = false;
		boolean hasPeriod = false;
		for (int i = 0; i < email.length(); i++) {
			if (email.charAt(i) == '@') {
				if (hasAt) {
					return "Invalid email!\n";
				}
				if (chars < 1) {
					return "Invalid email!\n";
				}
				hasAt = true;
			} else if (email.charAt(i) == '.') {
				if (!hasAt) {
					return "Invalid email!\n";
				}
				if (hasPeriod) {
					return "Invalid email!\n";
				}
				hasPeriod = true;
				chars = 0;
			} else {
				chars++;
			}
		}

		if (chars >= 2 && hasAt && hasPeriod) {
			return "";
		}
		return "Invalid email!\n";
	}

	/** validate the user's enter password */
	public String checkPW(String pw) {
		if (pw.length() < 4)
			return "Invalid password!\n";

		boolean hasNumber = false;
		boolean hasLower = false;
		boolean hasUpper = false;

		for (int i = 0; i < pw.length(); i++) {
			if (Character.isDigit(pw.charAt(i)))
				hasNumber = true;
			if (Character.isUpperCase(pw.charAt(i)))
				hasUpper = true;
			if (Character.isLowerCase(pw.charAt(i)))
				hasLower = true;
		}

		if (hasNumber && hasLower && hasUpper)
			return "";

		return "Invalid password!\n";
	}

	/** validate the user's entered zipcode */
	public String checkZip(String zip) {
		if (zip.length() < 3 || zip.length() > 5)
			return "Invalid zip!\n";
		for (int i = 0; i < zip.length(); i++) {
			if (!Character.isDigit(zip.charAt(i)))
				return "Invalid zip!\n";
		}
		return "";
	}

	/** validate the user's entered birth year */
	public String checkBirth(String date) {
		try {
			int year = Integer.parseInt(date); //the code that can fail
			int currentYear = Calendar.getInstance().get(Calendar.YEAR);
			if (year < currentYear - 120 || year > currentYear - 5) {
				return "Invalid date!\n";
			}
			return "";
		} catch (NumberFormatException ex) {
			return "Invalid date!\n";
		}
	}

	/** validate the user's entered phone number */
	public String checkPhone(String phone) {
		String newPhone = phone.replace("-", "");
		if (isNumeric(newPhone) && newPhone.length() == 10)
			return "";
		return "Invalid phone!\n";
	}

	/** main method for testing / setting up the GUI */
	public static void main(String[] args)
	{
		ValidateForm validateForm = new ValidateForm();

		System.out.println(validateForm.isAllAlpha("asdfaasdf"));
		System.out.println(validateForm.isAllAlpha("asdfaasdf1"));
		System.out.println(validateForm.isNumeric("1332324"));
		System.out.println(validateForm.isNumeric("asdfadfs"));
		System.out.println(validateForm.isNumeric("415.214"));
		System.out.println(validateForm.isNumeric("352.52.6"));

		//set up the GUI, you don't need to understand this code
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				//the frame is the GUI, it uses a ValidateForm object
				TextComponentFrame frame = new TextComponentFrame(new ValidateForm());
				frame.setVisible(true);
			}
		});
	}
}










/**
 * A frame (GUI) with simple text components to simulate a web form
 */
class TextComponentFrame extends JFrame
{
	static final int DEFAULT_WIDTH  = 300;
	static final int DEFAULT_HEIGHT = 400;

	ValidateForm validater;

	public TextComponentFrame(ValidateForm v)
	{
		validater = v;

		initGUI();

		super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		super.setLocationRelativeTo(null); //center on screen
	}

	/** initialize the GUI components, e.g. buttons and text fields */
	private void initGUI()
	{
		setTitle("Subscription Form");
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

		final JTextField personName = new JTextField();
		final JTextField emailField = new JTextField();
		final JTextField zipCode    = new JTextField();
		final JTextField birthDate  = new JTextField();

		MaskFormatter mfor = null;
		try {
			mfor = new MaskFormatter("###-###-####"); //for the phone number field
		}
		catch (ParseException e) {}

		final JFormattedTextField phoneNumber   = new JFormattedTextField(mfor);
		final JPasswordField      passwordField = new JPasswordField();

		JPanel northPanel = new JPanel();
		northPanel.setLayout(new GridLayout(6, 6)); //dimensions of layout
		northPanel.add(new JLabel("Name :  ", SwingConstants.RIGHT));
		northPanel.add(personName);
		northPanel.add(new JLabel("Password :  ", SwingConstants.RIGHT));
		northPanel.add(passwordField);
		northPanel.add(new JLabel("Email : ", SwingConstants.RIGHT));
		northPanel.add(emailField);
		northPanel.add(new JLabel("Zip Code (US) : ", SwingConstants.RIGHT));
		northPanel.add(zipCode);
		northPanel.add(new JLabel("Year of Birth: ", SwingConstants.RIGHT));
		northPanel.add(birthDate);
		northPanel.add(new JLabel("Phone Number: ", SwingConstants.RIGHT));
		northPanel.add(phoneNumber);

		add(northPanel, BorderLayout.NORTH);

		final JTextArea textArea = new JTextArea(8, 40);
		JScrollPane scrollPane   = new JScrollPane(textArea);

		add(scrollPane, BorderLayout.CENTER);

		// add button to listen for events, append text into the text area

		JPanel  southPanel   = new JPanel();
		JButton submitButton = new JButton("Submit");
		southPanel.add(submitButton);
		submitButton.addActionListener(new ActionListener() //make anonymous action listener
		{
			/** this method is called in response to an event, in this case the "Submit" button pressed */
			public void actionPerformed(ActionEvent event)
			{
				String name     = personName.getText() ;
				String email    = emailField.getText();
				String zip      = zipCode.getText();
				String birth    = birthDate.getText();
				String phone    = phoneNumber.getText();
				String password = new String(passwordField.getPassword());

				String result = "";

				result += validater.checkName(name);
				result += validater.checkPW(password);
				result += validater.checkEmail(email);
				result += validater.checkZip(zip);
				result += validater.checkBirth(birth);
				result += validater.checkPhone(phone);

				if (result.length() == 0)
					result = "Input accepted!";

				textArea.setText(result);
			}
		});

		add(southPanel, BorderLayout.SOUTH);
	}
}