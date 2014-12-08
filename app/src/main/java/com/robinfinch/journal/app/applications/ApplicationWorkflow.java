package com.robinfinch.journal.app.applications;

import com.robinfinch.journal.domain.workflow.Action;
import com.robinfinch.journal.domain.workflow.State;
import com.robinfinch.journal.domain.workflow.Workflow;

/**
 * Workflow of a job application.
 *
 * @author Mark Hoogenboom
 */
public class ApplicationWorkflow extends Workflow {

    public ApplicationWorkflow() {
        State prospect = defineInitialState(1001, "Prospect");
        State applied = defineState(1002, "Applied");
        State interview = defineState(1003, "Interview");
        State feedback = defineState(1004, "Waiting for feedback");
        State offerReceived = defineState(1005, "Offer received");
        State offerAccepted = defineState(1006, "Offer accepted");
        State declined = defineState(1007, "Declined");
        State rejected = defineState(1008, "Rejected");

        Action apply = defineAction(2001, "Apply");
        Action scheduleInterview = defineAction(2002, "Schedule interview");
        Action postponeInterview = defineAction(2003, "Postpone interview");
        Action attendInterview = defineAction(2004, "Attend interview");
        Action offer = defineAction(2005, "Offer");
        Action accept = defineAction(2006, "Accept");
        Action decline = defineAction(2007, "Decline");
        Action reject = defineAction(2008, "Reject");

        defineTransition(prospect, apply, applied);
        defineTransition(prospect, decline, declined);
        defineTransition(applied, scheduleInterview, interview);
        defineTransition(applied, reject, rejected);
        defineTransition(interview, attendInterview, feedback);
        defineTransition(interview, postponeInterview, interview);
        defineTransition(interview, decline, declined);
        defineTransition(feedback, scheduleInterview, interview);
        defineTransition(feedback, offer, offerReceived);
        defineTransition(feedback, reject, rejected);
        defineTransition(offerReceived, accept, offerAccepted);
        defineTransition(offerReceived, decline, declined);
    }
}
