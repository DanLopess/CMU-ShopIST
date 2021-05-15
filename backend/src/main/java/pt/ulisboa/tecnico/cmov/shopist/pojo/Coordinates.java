package pt.ulisboa.tecnico.cmov.shopist.pojo;

import lombok.Data;


@Data
public class Coordinates {
    private long latitude;
    private long longitude;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinates coordinates = (Coordinates) o;
        return this.distance(coordinates) <= 2;
    }

    public double distance(Coordinates coordinates) {
        if ((this.latitude == coordinates.latitude) && (this.longitude == coordinates.longitude)) {
            return 0;
        }
        else {
            double theta = this.longitude - coordinates.longitude;
            double dist = Math.sin(Math.toRadians(this.latitude)) * Math.sin(Math.toRadians(coordinates.latitude)) +
                    Math.cos(Math.toRadians(this.latitude)) * Math.cos(Math.toRadians(coordinates.latitude)) *
                            Math.cos(Math.toRadians(theta));
            dist = Math.acos(dist);
            dist = Math.toDegrees(dist);
            dist = dist * 60 * 1.1515;
            dist = dist * 1.609344;
            dist = dist * 1000; /* to meters */
            return (dist);
        }
    }
}
