package com.bitrider.survey.GeoDB;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;

@Entity(tableName = "places")
public class GeoBean implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @Ignore
    private int feedlist;
    private String airport;
    private String colloquial_area;
    private String establishment;
    private String neighborhood;
    private String park;
    private String point_of_interest;
    private String premise;
    private String route;
    private String street_number;
    private String sublocality;

    @NonNull
    private String title;
    private double lat;
    private double lng;

    @ColumnInfo(name="fullName")
    private String full_name;

    @ColumnInfo(name="longName")
    private String long_name;

    @ColumnInfo(name="geometry")
    private String geometry_type;

    @Ignore
    public GeoBean (String title, double lat, double lng, String full_name, String long_name, String geometry_type,
                    String airport,
                    String colloquial_area,
                    String establishment,
                    String neighborhood,
                    String park,
                    String point_of_interest,
                    String premise,
                    String route,
                    String street_number,
                    String sublocality,
                    int feedlist){
        this.title = title;
        this.lat = lat;
        this.lng = lng;
        this.full_name = full_name;
        this.long_name = long_name;
        this.geometry_type = geometry_type;
        this.airport = airport;
        this.colloquial_area = colloquial_area;
        this.establishment = establishment;
        this.neighborhood = neighborhood;
        this.park = park;
        this.point_of_interest = point_of_interest;
        this.premise = premise;
        this.route = route;
        this.street_number = street_number;
        this.sublocality = sublocality;
        this.feedlist = feedlist;

    }

    public GeoBean(){

    }


    public void setTitle(String title) {
        this.title = title;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getLong_name() {
        return long_name;
    }

    public void setLong_name(String long_name) {
        this.long_name = long_name;
    }

    public String getGeometry_type() {
        return geometry_type;
    }

    public void setGeometry_type(String geometry_type) {
        this.geometry_type = geometry_type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAirport() {
        return airport;
    }

    public void setAirport(String airport) {
        this.airport = airport;
    }

    public String getColloquial_area() {
        return colloquial_area;
    }

    public void setColloquial_area(String colloquial_area) {
        this.colloquial_area = colloquial_area;
    }

    public String getEstablishment() {
        return establishment;
    }

    public void setEstablishment(String establishment) {
        this.establishment = establishment;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public String getPark() {
        return park;
    }

    public void setPark(String park) {
        this.park = park;
    }

    public String getPoint_of_interest() {
        return point_of_interest;
    }

    public void setPoint_of_interest(String point_of_interest) {
        this.point_of_interest = point_of_interest;
    }

    public String getPremise() {
        return premise;
    }

    public void setPremise(String premise) {
        this.premise = premise;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getStreet_number() {
        return street_number;
    }

    public void setStreet_number(String street_number) {
        this.street_number = street_number;
    }

    public String getSublocality() {
        return sublocality;
    }

    public void setSublocality(String sublocality) {
        this.sublocality = sublocality;
    }

    public int getFeedlist() { return feedlist; }

    public void setFeedlist(int feedlist) { this.feedlist = feedlist; }

    @NonNull
    public String getTitle() {
        return title;
    }
}
