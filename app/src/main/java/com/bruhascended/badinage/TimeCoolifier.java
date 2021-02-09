package com.bruhascended.badinage;

import java.util.Calendar;

class TimeCoolifier {
    TimeCoolifier() {
    }

    static String coolifyThis(String curTime) {
        String returnThis = curTime;
        Calendar cal = Calendar.getInstance();
        cal.add(5, 1);
        String[] date = cal.getTime().toString().split(" ");
        Integer nDate = Integer.valueOf(Integer.valueOf(date[2]).intValue() - 1);
        String[] ne = curTime.split(" ");
        if (!ne[1].equals(date[1]) || !nDate.equals(Integer.valueOf(ne[2]))) {
            return returnThis;
        }
        return makePM(ne[0]);
    }

    private static String makePM(String ja) {
        String[] dfsa = ja.split(":");
        if (Integer.valueOf(dfsa[0]).intValue() > 12) {
            return (Integer.valueOf(dfsa[0]).intValue() - 12) + ":" + dfsa[1] + " PM";
        }
        return ja + " AM";
    }
}
