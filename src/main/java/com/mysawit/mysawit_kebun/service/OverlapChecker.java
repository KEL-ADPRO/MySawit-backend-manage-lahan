package com.mysawit.mysawit_kebun.service;

import com.mysawit.mysawit_kebun.model.Area;
import com.mysawit.mysawit_kebun.model.Koordinat;
import org.springframework.stereotype.Component;

@Component
public class OverlapChecker {

    private static class Vector2D {
        private final double x;
        private final double y;

        Vector2D(double x, double y) {
            this.x = x;
            this.y = y;
        }

        Vector2D subtract(Vector2D other) {
            return new Vector2D(this.x - other.x, this.y - other.y);
        }

        Vector2D normal() {
            return new Vector2D(-this.y, this.x);
        }

        double dot(Vector2D other) {
            return this.x * other.x + this.y * other.y;
        }
    }

    private static class Projection {
        private final double min;
        private final double max;

        Projection(double min, double max) {
            this.min = min;
            this.max = max;
        }
    }

    public boolean checkOverlap(Area a, Area b) {
        Vector2D[] polyA = getVertices(a);
        Vector2D[] polyB = getVertices(b);

        // Test separating axes from polygon A's edges
        if (hasSeparatingAxis(polyA, polyB)) {
            return false;
        }

        // Test separating axes from polygon B's edges
        if (hasSeparatingAxis(polyB, polyA)) {
            return false;
        }

        // No separating axis found -> the polygons overlap!
        return true;
    }

    private Vector2D[] getVertices(Area area) {
        return new Vector2D[] {
            toVector(area.getBottomLeft()),
            toVector(area.getBottomRight()),
            toVector(area.getTopRight()),
            toVector(area.getTopLeft())
        };
    }

    private Vector2D toVector(Koordinat k) {
        return new Vector2D(k.getX(), k.getY());
    }

    private boolean hasSeparatingAxis(Vector2D[] poly1, Vector2D[] poly2) {
        int len = poly1.length;
        for (int i = 0; i < len; i++) {
            Vector2D p1 = poly1[i];
            Vector2D p2 = poly1[(i + 1) % len];

            Vector2D edge = p2.subtract(p1);
            Vector2D axis = edge.normal();

            Projection proj1 = project(poly1, axis);
            Projection proj2 = project(poly2, axis);

            // If the projections do not overlap, we have found a separating axis.
            // Touching borders exactly on the edge is considered NOT overlapping.
            if (proj1.max <= proj2.min || proj2.max <= proj1.min) {
                return true;
            }
        }
        return false;
    }

    private Projection project(Vector2D[] poly, Vector2D axis) {
        double min = poly[0].dot(axis);
        double max = min;
        for (int i = 1; i < poly.length; i++) {
            double p = poly[i].dot(axis);
            if (p < min) {
                min = p;
            }
            if (p > max) {
                max = p;
            }
        }
        return new Projection(min, max);
    }
}
