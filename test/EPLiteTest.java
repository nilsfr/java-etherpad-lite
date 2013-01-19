import org.etherpad_lite_client.*;
import java.util.HashMap;

public class EPLiteTest {
	private EPLiteClient client;

	public static void main(String[] args) {
		EPLiteTest tester = new EPLiteTest(args[0], args[1]);
		tester.run();
	}

	EPLiteTest(String url, String apiKey) {
		client = new EPLiteClient(url, apiKey);
	}

	public void run() {
		this.print(this.testPadContents());
		this.print(this.testListAllPads());
		System.out.print("\n");
	}

	private boolean testPadContents() {
		client.createPad("java_test_pad");
		client.setText("java_test_pad", "foo!!!");
		HashMap pad = client.getText("java_test_pad");
		client.deletePad("java_test_pad");

		String text = pad.get("text").toString();
		return text.equals("foo!!!\n");
	}

	private boolean testListAllPads() {
		client.createPad("java_test_pad_1");
		client.createPad("java_test_pad_2");
		HashMap result = client.listAllPads();
		client.deletePad("java_test_pad_1");
		client.deletePad("java_test_pad_2");

		String[] padIDs = (String[]) result.get("padIDs");
		return padIDs[0].equals("java_test_pad_1");
	}

	private void print(boolean passed) {
		if ( passed ) {
			System.out.print(".");
		} else {
			System.out.print("F");
		}
	}
}
