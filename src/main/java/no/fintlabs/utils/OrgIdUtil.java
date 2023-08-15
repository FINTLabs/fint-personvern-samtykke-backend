package no.fintlabs.utils;

public class OrgIdUtil {

    public static String getFromTopic(String topicName){
        return topicName.substring(0, topicName.indexOf("."));
    }

    public static String uniform(String orgId){
        return orgId.replace("-", "_");
    }

    public static String toKafkaTopic(String orgId){
        return orgId.replace("_", "-");
    }

    public static String toKafkaEvent(String orgId){
        int lastUnderscore = orgId.lastIndexOf("_");
        if (lastUnderscore == -1) {
            return orgId;
        }

        String beforeLastUnderscore = orgId.substring(0, lastUnderscore);
        String afterLastUnderscore = orgId.substring(lastUnderscore + 1);

        beforeLastUnderscore = beforeLastUnderscore.replace("_", "-");

        return beforeLastUnderscore + "." + afterLastUnderscore;
    }
}
