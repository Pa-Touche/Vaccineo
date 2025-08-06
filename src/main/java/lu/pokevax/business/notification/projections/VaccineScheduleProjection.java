package lu.pokevax.business.notification.projections;

public interface VaccineScheduleProjection {

    Integer getDoseNumber();

    VaccineTypeProjection getVaccineType();
}
