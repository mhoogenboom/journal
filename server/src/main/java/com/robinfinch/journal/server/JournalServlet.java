package com.robinfinch.journal.server;

import com.robinfinch.journal.domain.JournalEntry;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import javax.annotation.security.DeclareRoles;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Mark Hoogenboom
 */
@WebServlet(name = "JournalServlet", urlPatterns = {"/display"})
@DeclareRoles("journalowner")
@ServletSecurity(
        @HttpConstraint(
                transportGuarantee = ServletSecurity.TransportGuarantee.NONE,
                rolesAllowed = {"journalowner"})
)
public class JournalServlet extends HttpServlet {

    @PersistenceContext(unitName = "JournalServerPU")
    private EntityManager em;

    @Override
    public String getServletInfo() {
        return "Journal";
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getUserPrincipal().getName();

        List<JournalEntry> entries = new ArrayList<>(findEntries(email));

        Collections.sort(entries);

        response.setContentType("text/plain;charset=UTF-8");

        try (PrintWriter out = response.getWriter()) {
            printJournalHeader(out);

            Calendar c = Calendar.getInstance(TimeZone.getTimeZone("Europe/London"), Locale.UK);

            int year = -1;
            int month = -1;
            int day = -1;
            int i = 0;

            for (JournalEntry entry : entries) {
                c.setTime(entry.getDayOfEntry());

                if (year != c.get(Calendar.YEAR)) {
                    year = c.get(Calendar.YEAR);
                    month = -1;
                }

                if (month != c.get(Calendar.MONTH)) {
                    printMonthHeader(out, c);
                    month = c.get(Calendar.MONTH);
                    day = -1;
                }

                if (day != c.get(Calendar.DAY_OF_MONTH)) {
                    printDayHeader(out, c);
                    day = c.get(Calendar.DAY_OF_MONTH);
                    i = 0;
                }

                printEntry(out, i++, entry);
            }

            printJournalFooter(out);
        }
    }

    private List<JournalEntry> findEntries(String email) {
        return em.createQuery("SELECT e FROM JournalEntry e WHERE e.owner.email = ?1", JournalEntry.class)
                .setParameter(1, email)
                .getResultList();
    }

    private void printJournalHeader(PrintWriter out) {
        out.println("Journal");
    }

    private void printMonthHeader(PrintWriter out, Calendar c) {
        out.println();
        out.print(String.format("%1$tB %1$tY", c));
    }

    private void printDayHeader(PrintWriter out, Calendar c) {
        out.println();
        out.println(String.format("%1$tA %1$te", c));
    }

    private void printEntry(PrintWriter out, int i, JournalEntry entity) {
        if (i > 0) {
            out.print(" ");
        }
        out.print(entity.toPrettyString());
    }

    private void printJournalFooter(PrintWriter out) {
        out.println();
    }
}
