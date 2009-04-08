package org.genedb.web.tags.misc;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class DateCreationTag extends SimpleTagSupport {

    private static final Logger logger = Logger.getLogger(DateCreationTag.class);

    private long time;

    private String message;


    @Override
    public void doTag() throws JspException, IOException {

        if (time == 0) {
            return;
        }

        Date date = new Date(time);
        PageContext pc = (PageContext) getJspContext();
        JspWriter out = pc.getOut();
        out.println(message + " " + DateFormat.getDateInstance(DateFormat.MEDIUM).format(date));
    }

    public void setTime(long time) {
        this.time = time;
    }


    public String getMessage() {
        return message;
    }


    public void setMessage(String message) {
        this.message = message;
    }

}