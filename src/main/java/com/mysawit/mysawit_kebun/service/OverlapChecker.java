package com.mysawit.mysawit_kebun.service;

import com.mysawit.mysawit_kebun.model.Area;
import org.springframework.stereotype.Component;

@Component
public class OverlapChecker {
    public boolean checkOverlap(Area a, Area b) {
        double aMinX = Math.min(a.getBottomLeft().getX(), a.getTopLeft().getX());
        double aMaxX = Math.max(a.getBottomRight().getX(), a.getTopRight().getX());
        double aMinY = Math.min(a.getBottomLeft().getY(), a.getBottomRight().getY());
        double aMaxY = Math.max(a.getTopLeft().getY(), a.getTopRight().getY());

        double bMinX = Math.min(b.getBottomLeft().getX(), b.getTopLeft().getX());
        double bMaxX = Math.max(b.getBottomRight().getX(), b.getTopRight().getX());
        double bMinY = Math.min(b.getBottomLeft().getY(), b.getBottomRight().getY());
        double bMaxY = Math.max(b.getTopLeft().getY(), b.getTopRight().getY());

        return aMaxX > bMinX && bMaxX > aMinX && aMaxY > bMinY && bMaxY > aMinY;
    }
}
