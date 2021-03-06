package com.datang.business;/* Copyright 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import android.content.Context;
import android.content.Intent;

import com.datang.business.measurements.HttpTask;
import com.datang.business.measurements.PingTask;

import java.io.InvalidClassException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.Callable;

/**
 * Represents a scheduled measurement task. Subclasses implement functionality
 * for performing the actual measurement. Comparable interface allow comparison
 * inside the priority queue.
 *
 * @author mdw@google.com (Matt Welsh)
 * @author wenjiezeng@google.com (Wenjie Zeng)
 */
@SuppressWarnings("rawtypes")
public abstract class MeasurementTask implements Callable<MeasurementResult>, Comparable {
    // the priority queue we use puts the smallest element in the head of the queue
    public static final int USER_PRIORITY = Integer.MIN_VALUE;
    public static final int INVALID_PRIORITY = Integer.MAX_VALUE;
    public static final int INFINITE_COUNT = -1;

    public static final int WAIT_STATUS = -1;
    public static final int EXE_STATUS = 1;
    public static final int IDLE_STATUS = 0;
    public static final int END_STSATUS = 2;

    private static HashMap<String, Class> measurementTypes;
    // Maps between the type of task and its readable name
    private static HashMap<String, String> measurementDescToType;

    // TODO(Wenjie): Static initializer for type -> Measurement map
    // Add new measurement types here to enable them
    static {
        measurementTypes = new HashMap<String, Class>();
        measurementDescToType = new HashMap<String, String>();
        measurementTypes.put(PingTask.TYPE, PingTask.class);
        measurementDescToType.put(PingTask.DESCRIPTOR, PingTask.TYPE);
        measurementTypes.put(HttpTask.TYPE, HttpTask.class);
        measurementDescToType.put(HttpTask.DESCRIPTOR, HttpTask.TYPE);
    }

    protected MeasurementDesc measurementDesc;
    protected Context parent;
    /* When updating the 'progress' field, ensure that it's within the range between 0 and
     * com.datang.business.Config.MAX_PROGRESS_BAR_VALUE, inclusive. Values outside this range have special meanings and
     * can trigger unexpected results.
     */
    protected int progress;

    /**
     * @param measurementDesc
     * @param parent
     */
    protected MeasurementTask(MeasurementDesc measurementDesc, Context parent) {
        super();
        this.measurementDesc = measurementDesc;
        this.parent = parent;
        this.progress = 0;
    }

    /**
     * Gets the currently available measurement types
     */
    public static Set<String> getMeasurementNames() {
        return measurementDescToType.keySet();
    }

    /**
     * Get the type of a measurement based on its name. Type is for JSON interface only
     * where as measurement name is a readable string for the UI
     */
    public static String getTypeForMeasurementName(String name) {
        return measurementDescToType.get(name);
    }

    public static Class getTaskClassForMeasurement(String type) {
        return measurementTypes.get(type);
    }

    /* This is put here for consistency that all com.datang.business.MeasurementTask should
     * have a getDescClassForMeasurement() method. However, the com.datang.business.MeasurementDesc is abstract
     * and cannot be instantiated */
    public static Class getDescClass() throws InvalidClassException {
        throw new InvalidClassException("getDescClass() should only be invoked on "
                + "subclasses of com.datang.business.MeasurementTask.");
    }

    /* Compare priority as the first order. Then compare start time.*/
    @Override
    public int compareTo(Object t) {
        MeasurementTask another = (MeasurementTask) t;
        Long myPrority = this.measurementDesc.priority;
        Long anotherPriority = another.measurementDesc.priority;
        int priorityComparison = myPrority.compareTo(anotherPriority);
        if (priorityComparison == 0 &&
                this.measurementDesc.startTime != null && another.measurementDesc.startTime != null) {
            return this.measurementDesc.startTime.compareTo(another.measurementDesc.startTime);
        } else {
            return priorityComparison;
        }
    }

    public long timeFromExecution() {
        return this.measurementDesc.startTime.getTime() - System.currentTimeMillis();
    }

    public boolean isPassedDeadline() {
        if (this.measurementDesc.endTime == null) {
            return false;
        } else {
            long endTime = this.measurementDesc.endTime.getTime();
            return endTime <= System.currentTimeMillis();
        }
    }

    public String getMeasurementType() {
        return this.measurementDesc.type;
    }

    public MeasurementDesc getDescription() {
        return this.measurementDesc;
    }

    /**
     * Returns a brief human-readable descriptor of the task.
     */
    public abstract String getDescriptor();

    @Override
    public abstract MeasurementResult call() throws MeasurementError;

    /**
     * Return the string indicating the measurement type.
     */
    public abstract String getType();

    /* Place holder in case user wants to view the progress of active com.datang.business.measurements*/
    public int getProgress() {
        return this.progress;
    }

    @Override
    public String toString() {
        String result = "[Measurement " + getDescriptor() + " scheduled to run at " +
                getDescription().startTime + "]";

        return this.measurementDesc.toString();
    }

    @Override
    public abstract MeasurementTask clone();

    /**
     * Stop the measurement, even when it is running. There is no side effect
     * if the measurement has not started or is already finished.
     */
    public abstract void stop();

    public void broadcastProgressForUser(int progress) {
        if (measurementDesc.priority == MeasurementTask.USER_PRIORITY) {
            Intent intent = new Intent();
            intent.setAction(UpdateIntent.MEASUREMENT_PROGRESS_UPDATE_ACTION);
            intent.putExtra(UpdateIntent.TASK_KEY, measurementDesc.key);
            intent.putExtra(UpdateIntent.TASK_TYPE, measurementDesc.getType());
            intent.putExtra(UpdateIntent.PROGRESS_PAYLOAD, progress);
            intent.putExtra(UpdateIntent.TASK_PRIORITY_PAYLOAD, MeasurementTask.USER_PRIORITY);
            parent.sendBroadcast(intent);

            Intent statIntent = new Intent();
            statIntent.setAction(UpdateIntent.SYSTEM_STATUS_UPDATE_ACTION);
            statIntent.putExtra(UpdateIntent.TASK_INDEX, measurementDesc.name);
//            intent.setAction(UpdateIntent.SYSTEM_STATUS_UPDATE_ACTION);
            statIntent.putExtra(UpdateIntent.STATUS_MSG_PAYLOAD, MeasurementTask.EXE_STATUS);
            statIntent.putExtra(UpdateIntent.TASK_LAST_TIME, new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()));
            parent.sendBroadcast(statIntent);
        }
    }
}
