package com.github.bevilacqua1996.matchers;

import com.github.bevilacqua1996.utils.DataUtils;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.github.bevilacqua1996.utils.DataUtils.obterDataComDiferencaDias;

public class DiaPosteriorSemanaMatcher extends TypeSafeMatcher<Date> {

    private Integer diasAcrescentar;

    public DiaPosteriorSemanaMatcher(Integer diasAcrescentar) {
        this.diasAcrescentar = diasAcrescentar;
    }

    @Override
    protected boolean matchesSafely(Date date) {
        return DataUtils.isMesmaData(date, obterDataComDiferencaDias(diasAcrescentar));
    }

    @Override
    public void describeTo(Description description) {
        Calendar data = Calendar.getInstance();
        data.setTime(obterDataComDiferencaDias(diasAcrescentar));
        String dataExtenso = data.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, new Locale("pt", "BR"));
        description.appendText(dataExtenso);
    }
}
