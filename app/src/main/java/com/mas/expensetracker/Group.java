package com.mas.expensetracker;

import java.util.ArrayList;

public class Group {
    public String groupDescription;
    public String groupName;
    public ArrayList<String> participantEmails;

    public Group(String groupName, String groupDescription, ArrayList<String> participantEmails) {
        this.groupName = groupName;
        this.groupDescription = groupDescription;
        this.participantEmails = participantEmails;
    }

}
