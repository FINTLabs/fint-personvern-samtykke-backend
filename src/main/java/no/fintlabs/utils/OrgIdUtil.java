package no.fintlabs.utils;

public class OrgIdUtil {

    public static String getFromTopic(String topicName){
        return topicName.substring(0, topicName.indexOf("."));
    }

    public static String uniform(String orgId){
        return orgId.replace("-", "_");
    }

    public static String uniformForKafka(String orgId){
        return orgId.replace("_", "-");
    }
}
