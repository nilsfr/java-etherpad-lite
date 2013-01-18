import org.etherpad_lite_client.*;
import java.util.HashMap;

public class EPLiteTest {
	private EPLiteClient client;

	public static void main(String[] args) {
		EPLiteTest tester = new EPLiteTest(args[0], args[1]);
		tester.run();
	}

	EPLiteTest(String url, String api_key) {
		client = new EPLiteClient(url, api_key);
	}

	public void run() {
		this.print_result(this.test_pads());
		System.out.print("\n");
	}

	private boolean test_pads() {
		client.createPad("java_test_pad");
		client.setText("java_test_pad", "foo!!!");
		HashMap pad = client.getText("java_test_pad");
		client.deletePad("java_test_pad");

		String text = pad.get("text").toString();
		return text.equals("foo!!!\n");
	}

	private void print_result(boolean passed) {
		if ( passed ) {
			System.out.print(".");
		} else {
			System.out.print("F");
		}
	}
}
