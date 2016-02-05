package com.example.nano1.dmvheads;

import android.test.suitebuilder.TestSuiteBuilder;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Created by nano1 on 2/5/2016.
 */
public class FullTestSuite extends TestSuite {
    public static Test suite(){
        return new TestSuiteBuilder(FullTestSuite.class)
                .includeAllPackagesUnderHere().build();
    }
}
