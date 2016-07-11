package com.bbd.saas.utils;


import com.bbd.poi.api.vo.MapPoint;
import com.bbd.saas.mongoModels.Site;

import java.util.ArrayList;
import java.util.List;

public class GeoUtil {

    public static List<Site> sortPoint(MapPoint start, List<Site> ends){
        List<Site> routes =new ArrayList<Site>();
        if(ends.size() >1){
            int count = 0;
            int size = ends.size();
            while(count < size){
                Site base = null;
                if(routes.size() > 0){
                    base = routes.get(count-1);
                }
                double minDistance = 0.0;
                Site tmp = null;
                for(Site point :ends){
                    double d1 = 0.0;
                    if(base == null){
                        d1 = GeoUtil.getDistance(start.getLng(),start.getLat(),Double.parseDouble(point.getLng()),Double.parseDouble(point.getLat()));
                    }else{
                        d1 = GeoUtil.getDistance(Double.parseDouble(base.getLng()),Double.parseDouble(base.getLat()),Double.parseDouble(point.getLng()),Double.parseDouble(point.getLat()));
                    }
                    if(minDistance == 0){
                        minDistance = d1;
                        tmp = point;
                    }else if(d1 != 0 && d1 < minDistance){
                        tmp = point;
                        minDistance = d1;
                    }
                }
                routes.add(tmp);
                ends.remove(tmp);
                count++;
            }
        }else{
            routes = ends;
        }
        return routes;
    }


    public static double getDistance(double lngFrom, double latFrom,   double lngTo,double latTo) {
        // earth's mean radius in KM  
        double r = 6378.137;  
        latFrom = Math.toRadians(latFrom);
		lngFrom = Math.toRadians(lngFrom);
		latTo = Math.toRadians(latTo);
		lngTo = Math.toRadians(lngTo);
        double d1 = Math.abs(latFrom - latTo);
        double d2 = Math.abs(lngFrom - lngTo);
        double p = Math.pow(Math.sin(d1 / 2), 2) + Math.cos(latFrom)
                * Math.cos(latTo) * Math.pow(Math.sin(d2 / 2), 2);
        double dis = r * 2 * Math.asin(Math.sqrt(p));  
        return dis;  
    }
}  
