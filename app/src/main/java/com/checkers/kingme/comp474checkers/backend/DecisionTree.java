package com.checkers.kingme.comp474checkers.backend;

import java.util.Arrays;

/**
 * Created by Richa on 4/12/2015.
 */
public class DecisionTree {

    private Move[] kAr;
    int k;

    public DecisionTree(Move[] kAr, int k) {
        this.kAr = kAr;
        this.k = k;
    }

    public Move[] getKAr() {
        return kAr;
    }

    /**
     *
     * @return array with deleted node
     */
    public Move[] deleteMax() {
        int n = kAr.length;

        if (n == 0) {
            System.out.println("Heap is empty");
        } else {
            Move maxElement = kAr[0];
            System.out.println("max element is" + maxElement);
            kAr[0] = kAr[n - 1];
            kAr = Arrays.copyOf(kAr, kAr.length - 1);
            n = n - 1;
            if (n == 0 || n == 1) {
                System.out.println("no need to traverse");

            } else {
                int parent = 0;
                int childIndex = 0;
                int i = 0;
                // parent
                while (i <= n) {

                    // finding max of child
                    maxElement.setPriority(0);

                    for (i = (parent * k + 1); i <= k * (parent + 1); i++) {
                        if (i <= (n - 1) && kAr[i].getPriority() > maxElement.getPriority()) {
                            maxElement = kAr[i];
                            childIndex = i;
                        }
                    }

                    //if child is greater swap its value with parent
                    if (maxElement.getPriority() > kAr[parent].getPriority()) {

                        kAr[childIndex] = kAr[parent];

                        kAr[parent] = maxElement;
                    }

                    //iterate for the next child
                    parent = childIndex;

                }
            }
        }
        return kAr;
    }

    /**
     *
     * @param value to insert
     * @return new array after insert
     */
    public Move[] insert(Move value) {
        int n = kAr.length;

        if (n == 0) {
            kAr = new Move[1];
            kAr[0] = value;
        } else {
            Move newElement =new Move();
            //System.out.println(value + " is inserted");

            kAr = Arrays.copyOf(kAr, n + 1);
            kAr[n] = value;
            n = n + 1;

            int parent = 0;
            int childIndex = n - 1;

            newElement.setPriority(0);

            while (childIndex != 0) {

                // finding parent of node

                if (childIndex % k == 0)
                    parent = (childIndex / k) - 1;
                else
                    parent = childIndex / k;

                //if child is greater swap its value with parent
                if (kAr[childIndex].getPriority() > kAr[parent].getPriority()) {

                    newElement = kAr[childIndex];
                    kAr[childIndex] = kAr[parent];
                    kAr[parent] = newElement;
                }

                //Iterate for the next parent
                childIndex = parent;
            }
        }
        return kAr;
    }

}
