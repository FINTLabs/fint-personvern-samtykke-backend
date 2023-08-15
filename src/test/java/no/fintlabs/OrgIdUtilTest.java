package no.fintlabs;

import no.fintlabs.utils.OrgIdUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrgIdUtilTest {

    @Test
    public void testGetOrgIdFromTopic() {
        String topicName = "org123.some.topic";

        String orgId = OrgIdUtil.getFromTopic(topicName);

        assertEquals("org123", orgId);
    }

    @Test
    public void testToKafkaEvent(){
        assertEquals("fintlabs.no", OrgIdUtil.toKafkaEvent("fintlabs_no"));
        assertEquals("trondelag-fylke.no", OrgIdUtil.toKafkaEvent("trondelag_fylke_no"));
        assertEquals("fintlabs.no", OrgIdUtil.toKafkaEvent("fintlabs.no"));
    }

    @Test
    public void testToKafkaTopic(){
        assertEquals("fintlabs-no", OrgIdUtil.toKafkaTopic("fintlabs_no"));
        assertEquals("trondelag-fylke-no", OrgIdUtil.toKafkaTopic("trondelag_fylke_no"));
    }

}
