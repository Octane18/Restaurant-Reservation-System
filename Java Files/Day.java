public class Day implements Cloneable, Comparable<Day> {

    private int year;
    private int month;
    private int day;
    final static String MonthNames =
            "JanFebMarAprMayJunJulAugSepOctNoDec";

    //Constructor
    public Day(int y, int m, int d) {
        this.year=y;
        this.month=m;
        this.day=d;
    }
    public Day(String sDay) {
        set(sDay);
    }
    public void set(String sDay) {
        String[] sDayParts = sDay.split("-");
        this.day = Integer.parseInt(sDayParts[0]);
        this.year = Integer.parseInt(sDayParts[2]);
        this.month = MonthNames.indexOf(sDayParts[1])/3+1;
    }

    // check if a given year is a leap year
    static public boolean isLeapYear(int y)
    {
        if (y%400==0)
            return true;
        else if (y%100==0)
            return false;
        else if (y%4==0)
            return true;
        else
            return false;
    }


    // check if y,m,d valid
    static public boolean valid(int y, int m, int d) {
        if (m<1 || m>12 || d<1) return false;
        switch(m){
            case 1: case 3: case 5: case 7:
            case 8: case 10: case 12:
                return d<=31;
            case 4: case 6: case 9: case 11:
                return d<=30;
            case 2:
                if (isLeapYear(y))
                    return d<=29;
                else
                    return d<=28;
        }
        return false;
    }
    public void advance() {
        if(isEndOfMonth()) {
            if (month==12) {
                this.month = 1;
                this.day = 1;
                this.year += 1;
            }
            else{
                this.day = 1;
                this.month += 1;
            }
        }
        else {
            this.day += 1;
        }
    }
    public void previous() {
        if(month==1) {
            month = 12;
            year -= 1;
            day = 31;
        }
        else if(isStartOfMonth()){
            int[] possible = {28,29,30,31};
            month-=1;
            int i=0;
            day = possible[i];
            while(!valid(year, month, day)){
                i++;
                day = possible[i%4];
            }
        }
        else {
            day -=1;
        }
    }
    public boolean isEndOfMonth() {
        return !valid(year, month, day + 1);
    }
    public boolean isStartOfMonth() {
        return !valid(year, month, day-1);
    }

    // Return a string for the day like dd MMM yyyy
    public String toString() {

        final String[] MonthNames = {
                "Jan", "Feb", "Mar", "Apr",
                "May", "Jun", "Jul", "Aug",
                "Sep", "Oct", "Nov", "Dec"};

        return day+"-"+ MonthNames[month-1] + "-"+ year;
    }
    public int compare(Day another) {
        if(this.year==another.year) {
            if(this.month==another.month) {
                if(this.day==another.day) {
                    return 0;
                }
                else if(this.day>another.day) {
                    return 1;
                }
                else {
                    return -1;
                }
            }
            else if(this.month>another.month) {
                return 1;
            }
            else {
                return -1;
            }

        }
        else if (this.year>another.year) {
            return 1;
        }
        else {
            return -1;
        }
    }
    @Override
    public Day clone() {
        Day copy = null;
        try{
            copy =(Day) super.clone();
        }
        catch(CloneNotSupportedException e){
            e.printStackTrace();
        }
        return copy;
    }
    @Override
    public int hashCode() {
        return (year*1000+month*100+day)%11;
    }
    @Override
    public boolean equals(Object obj) {
        if(obj==null) return false;
        if(obj.getClass()!=this.getClass()) return false;
        if(obj==this) return true;
        Day other = (Day) obj;
        return this.year== other.year
                && this.month ==other.month
                && this.day == other.day;
    }
    @Override
    public int compareTo(Day other) {
        if(this.year==other.year) {
            if(this.month==other.month) {
                if(this.day==other.day) {
                    return 0;
                }
                else if(this.day>other.day) {
                    return 1;
                }
            }
            else if(this.month>other.month) {
                return 1;
            }
        }
        else if(this.year>other.year) {
            return 1;
        }
        return -1;
    }

}
