/*
 * ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2001 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in
 * the documentation and/or other materials provided with the
 * distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 * if any, must include the following acknowledgment:
 * "This product includes software developed by the
 * Apache Software Foundation (http://www.apache.org/)."
 * Alternately, this acknowledgment may appear in the software itself,
 * if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation" and
 * "Apache JMeter" must not be used to endorse or promote products
 * derived from this software without prior written permission. For
 * written permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache",
 * "Apache JMeter", nor may "Apache" appear in their name, without
 * prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 */
package org.apache.jmeter.visualizers;


// java
import java.text.DecimalFormat;

import org.apache.jmeter.samplers.SampleResult;


/**
 * Title:        RunningSample.java
 * Description:  Aggegate sample data container
 *
 * Class which is used to store aggregate sample data.
 * Just instantiate a new instance of this class, and then call addSample() a few times,
 * and pull the stats out with whatever methods you prefer.
 *
 * Copyright:    Copyright (c) 2001
 * Company:      Apache Foundation
 * @author James Boutcher
 * @version 1.0
 */
public class RunningSample
{

    private static DecimalFormat rateFormatter = new DecimalFormat("#.0");
    private static DecimalFormat errorFormatter = new DecimalFormat("#0.00%");

    private long counter;
    private long runningSum;
    private long max, min;
    private long errorCount;
    private long firstTime;
    private long lastTime;
    private String label;
    private int index;

    /**
     * use this constructor.
     */
    public RunningSample(String label, int index)
    {
        this.label = label;
        this.index = index;
        counter = 0L;
        runningSum = 0L;
        max = Long.MIN_VALUE;
        min = Long.MAX_VALUE;
        errorCount = 0L;
        firstTime = Long.MAX_VALUE;
        lastTime = 0L;
    }

    /**
     * Returns the throughput associated to this sampler in requests per second.
     * May be slightly skewed because it takes the timestamps of the first and
     * last samples as the total time passed, and the test may actually have
     * started before that start time and ended after that end time.
     **/
    public double getRate()
    {
        long howLongRunning = lastTime - firstTime;

        if (howLongRunning == 0) return Double.MAX_VALUE;
        return (double) counter / howLongRunning * 1000.0;
    }

    /**
     * Returns the throughput associated to this sampler in requests per min.
     * May be slightly skewed because it takes the timestamps of the first and
     * last samples as the total time passed, and the test may actually have
     * started before that start time and ended after that end time.
     **/
    public double getRatePerMin()
    {
        long howLongRunning = lastTime - firstTime;

        if (howLongRunning == 0) return Double.MAX_VALUE;
        return (double) counter / howLongRunning * 60000.0;
    }

    /**
     * Returns a String that represents the throughput associated for this
     * sampler, in units appropriate to its dimension:
     * <p>
     * The number is represented in requests/second or requests/minute or requests/hour.
     * <p>
     * Examples:
     *      "34.2/sec"
     *      "0.1/sec"
     *      "43.0/hour"
     *      "15.9/min"
     * @return a String representation of the rate the samples are being taken at.
     */
    public String getRateString()
    {
        double rate = getRate();

        if (rate == Double.MAX_VALUE) return "N/A";

        String unit = "sec";

        if (rate < 1.0)
        {
            rate *= 60.0;
            unit = "min";
        }
        if (rate < 1.0)
        {
            rate *= 60.0;
            unit = "/hour";
        }

        String rval = rateFormatter.format(rate) + "/" + unit;

        return (rval);
    }

    public String getLabel()
    {
        return label;
    }

    public int getIndex()
    {
        return index;
    }

    /**
     * Records a sample.
     * @arg aTimeInMillis Time in milliseconds that this sample took to process
     * @arg aSuccessFlag Flag for if this sample was successful or not
     */
    public synchronized void addSample(SampleResult res)
    {
        long aTimeInMillis = res.getTime();
        boolean aSuccessFlag = res.isSuccessful();

        counter++;
        long startTime = res.getTimeStamp() - aTimeInMillis;
        if (firstTime > startTime)
        {
            // this is our first sample, set the start time to current timestamp
            firstTime = startTime;
        }
        if(lastTime < res.getTimeStamp())
        {
            lastTime = res.getTimeStamp();
        }
        runningSum += aTimeInMillis;
        if (aTimeInMillis > max) max = aTimeInMillis;
        if (aTimeInMillis < min) min = aTimeInMillis;
        if (!aSuccessFlag) errorCount++;
    }

    /**
     * Returns the time in milliseconds of the quickest sample.
     * @return the time in milliseconds of the quickest sample.
     */
    public long getMin()
    {
        long rval = 0;

        if (min != Long.MAX_VALUE) rval = min;
        return (rval);
    }

    /**
     * Returns the time in milliseconds of the slowest sample.
     * @return the time in milliseconds of the slowest sample.
     */
    public long getMax()
    {
        long rval = 0;

        if (max != Long.MIN_VALUE) rval = max;
        return (rval);
    }

    /**
     * Returns the average time in milliseconds that samples ran in.
     * @return the average time in milliseconds that samples ran in.
     */
    public long getAverage()
    {
        if (counter == 0) return (0);
        return (runningSum / counter);
    }

    /**
     * Returns the number of samples that have been recorded by this instance of the RunningSample class.
     * @return the number of samples that have been recorded by this instance of the RunningSample class.
     */
    public long getNumSamples()
    {
        return (counter);
    }

    /**
     * Returns the raw double value of the percentage of samples with errors that were recorded.
     * (Between 0.0 and 1.0)
     * If you want a nicer return format, see getErrorPercentageString()
     *
     * @return the raw double value of the percentage of samples with errors that were recorded.
     */
    public double getErrorPercentage()
    {
        double rval = 0.0;

        if (counter == 0) return (rval);
        rval = (double) errorCount / (double) counter;
        return (rval);
    }

    /**
     * Returns a String which represents the percentage of sample errors that have occurred.
     * "0.00%" through "100.00%"
     * @return a String which represents the percentage of sample errors that have occurred.
     */
    public String getErrorPercentageString()
    {
        double myErrorPercentage = this.getErrorPercentage();

        return (errorFormatter.format(myErrorPercentage));
    }

    /**
     * For debugging purposes, mainly.
     */
    public String toString()
    {
        StringBuffer mySB = new StringBuffer();

        mySB.append("Samples: " + this.getNumSamples() + "  ");
        mySB.append("Avg: " + this.getAverage() + "  ");
        mySB.append("Min: " + this.getMin() + "  ");
        mySB.append("Max: " + this.getMax() + "  ");
        mySB.append("Error Rate: " + this.getErrorPercentageString() + "  ");
        mySB.append("Sample Rate: " + this.getRateString());
        return (mySB.toString());
    }

} // class RunningSample
