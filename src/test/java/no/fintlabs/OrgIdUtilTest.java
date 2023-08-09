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
}
