package com.github.bevilacqua1996.matchers;

import java.util.Calendar;

public class MatchersProprios {

    public static DiaSemanaMatcher caiEm(Integer diaSemana) {
        return new DiaSemanaMatcher(diaSemana);
    }

    public static DiaPosteriorSemanaMatcher ehDiaPosterior(Integer diasAcrescentar) {
        return new DiaPosteriorSemanaMatcher(diasAcrescentar);
    }

    public static DiaPosteriorSemanaMatcher ehDiaSeguinte() {
        return new DiaPosteriorSemanaMatcher(1);
    }

    public static DiaSemanaMatcher caiNaSegunda() {
        return new DiaSemanaMatcher(Calendar.MONDAY);
    }

    public static DiaAtualSemanaMatcher ehHoje() {
        return new DiaAtualSemanaMatcher();
    }

}
