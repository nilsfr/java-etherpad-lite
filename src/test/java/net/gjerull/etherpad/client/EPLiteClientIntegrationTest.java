package net.gjerull.etherpad.client;

import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Integration test for simple App.
 */
public class EPLiteClientIntegrationTest {
    private EPLiteClient client;

    /**
     * Useless testing as it depends on a specific API key
     */
    @Before
    public void setUp() throws Exception {
        this.client = new EPLiteClient("http://localhost:9001", "K8OF91QMQYUvrNu3e9rJ7FnnVgaB3m9q");

    }

    @Test
    public void create_pad_set_and_get_content() {
        client.createPad("java_test_pad");
        client.setText("java_test_pad", "foo!!!");
        Map pad = client.getText("java_test_pad");
        client.deletePad("java_test_pad");

        String text = pad.get("text").toString();
        assertEquals("foo!!!\n", text);
    }

    @Test
    public void create_pads_and_list_them() {
        client.createPad("java_test_pad_1");
        client.createPad("java_test_pad_2");
        Map result = client.listAllPads();
        client.deletePad("java_test_pad_1");
        client.deletePad("java_test_pad_2");

        List padIDs = (List) result.get("padIDs");
        assertTrue(padIDs.size() > 2);
        assertTrue(padIDs.contains("java_test_pad_1"));
        assertTrue(padIDs.contains("java_test_pad_2"));
    }
}
