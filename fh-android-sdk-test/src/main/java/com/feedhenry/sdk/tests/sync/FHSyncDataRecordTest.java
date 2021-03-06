/**
 * Copyright Red Hat, Inc, and individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.feedhenry.sdk.tests.sync;

import junit.framework.TestCase;

import org.json.fh.JSONObject;

import com.feedhenry.sdk.sync.FHSyncDataRecord;

public class FHSyncDataRecordTest extends TestCase {

    public void testDataHappy() throws Exception {
        JSONObject obj = FHTestUtils.generateJSON();
        System.out.println("Generated json = " + obj.toString());
        FHSyncDataRecord record = new FHSyncDataRecord(obj);
        assertNotNull(record.getData());
        assertNotNull(record.getHashValue());

        JSONObject json = record.getJSON();
        FHSyncDataRecord another = FHSyncDataRecord.fromJSON(json);
        assertEquals(record, another);
    }

}
