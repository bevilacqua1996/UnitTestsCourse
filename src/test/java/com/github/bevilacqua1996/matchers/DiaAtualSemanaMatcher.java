package com.github.bevilacqua1996.matchers;

import com.github.bevilacqua1996.utils.DataUtils;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DiaAtualSemanaMatcher extends TypeSafeMatcher<Date> {


    @Override
    protected boolean matchesSafely(Date date) {
        return DataUtils.isMesmaData(date, new Date());
    }

    @Override
    public void describeTo(Description description) {
        Calendar data = Calendar.getInstance();
        data.setTime(new Date());
        String dataExtenso = data.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, new Locale("pt", "BR"));
        description.appendText(dataExtenso);
    }
}
