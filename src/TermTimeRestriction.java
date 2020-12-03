import java.util.Date;

public class TermTimeRestriction {
    private long termTimeRestrictionId;
    private Date restrictionStartDate;
    private Date restrictionEndDate;
    private int restrictionDuration;
    private Date weekdayBookingTimeRestriction;
    private Date weekendBookingTimeRestriction;

    public TermTimeRestriction(long termTimeRestrictionId, Date restrictionStartDate, Date restrictionEndDate, Date weekdayBookingTimeRestriction, Date weekendBookingTimeRestriction)
    {
        this.termTimeRestrictionId = termTimeRestrictionId;
        this.restrictionStartDate = restrictionStartDate;
        this.restrictionEndDate = restrictionEndDate;
        this.weekdayBookingTimeRestriction = weekdayBookingTimeRestriction;
        this.weekendBookingTimeRestriction = weekendBookingTimeRestriction;
    }


    public Date getRestrictionStartDate() {
        return restrictionStartDate;
    }

    public void setRestrictionStartDate(Date restrictionStartDate) {
        this.restrictionStartDate = restrictionStartDate;
    }

    public Date getRestrictionEndDate() {
        return restrictionEndDate;
    }

    public void setRestrictionEndDate(Date restrictionEndDate) {
        this.restrictionEndDate = restrictionEndDate;
    }

    public int getRestrictionDuration() {
        return restrictionDuration;
    }

    public void setRestrictionDuration(int restrictionDuration) {
        this.restrictionDuration = restrictionDuration;
    }

    public Date getWeekdayBookingTimeRestriction() {
        return weekdayBookingTimeRestriction;
    }

    public void setWeekdayBookingTimeRestriction(Date weekdayBookingTimeRestriction) {
        this.weekdayBookingTimeRestriction = weekdayBookingTimeRestriction;
    }

    public Date getWeekendBookingTimeRestriction() {
        return weekendBookingTimeRestriction;
    }

    public void setWeekendBookingTimeRestriction(Date weekendBookingTimeRestriction) {
        this.weekendBookingTimeRestriction = weekendBookingTimeRestriction;
    }

    public long getTermTimeRestrictionId() {
        return termTimeRestrictionId;
    }

    public void setTermTimeRestrictionId(long termTimeRestrictionId) {
        this.termTimeRestrictionId = termTimeRestrictionId;
    }
}
