/*
 * Copyright 2019 Johns Hopkins University
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
package org.dataconservancy.pass.loader.nihms.analysis;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Elliot Metsger (emetsger@jhu.edu)
 */
public class PacmAnalyzer {

    private static final Logger LOG = LoggerFactory.getLogger(PacmAnalyzer.class);

    public static void main(String[] args) throws Exception {

        File hmsFile = new File("/tmp/data/hms/compliant_nihmspubs_20190805091852.csv");
        File hsphFile = new File("/tmp/data/hsph/compliant_hsph_nihmspubs_20190805091825.csv");

        Reader hmsReader = new FileReader(hmsFile);
        List<CSVRecord> hmsRecords = CSVFormat.EXCEL.parse(hmsReader).getRecords();


        Iterator<CSVRecord> hmsItr = hmsRecords.iterator();
        StringBuilder duplicates = new StringBuilder();

        while (hmsItr.hasNext()) {
            CSVRecord hms = hmsItr.next();

            if (duplicates.length() > 0) {
                LOG.info(duplicates.toString());
                duplicates.delete(0, duplicates.length());
            }

            Iterator<CSVRecord> hsphItr = CSVFormat.EXCEL.parse(new FileReader(hsphFile)).getRecords().iterator();

            NEXT_HSPH_RECORD: while (hsphItr.hasNext()) {

                CSVRecord hsph = hsphItr.next();

                for (int field = 0; field < 4; field++) {
                    AtomicInteger atomicField = new AtomicInteger(field);
                    Optional<String> value = internValue(field, hms);
                    if (value.isPresent()) {
                        if (valueEquals(value.get(), atomicField.get(), hsph)) {
                            if (duplicates.length() == 0) {
                                duplicates = new StringBuilder(String.format(
                                        "Duplicate(s) of hms %s\n (hms: %s) --> %s\n", hms.getRecordNumber(), hms.getRecordNumber(), hms));
                            }
                            duplicates.append(String.format(" (hsph %s, via field %s) --> %s\n", hsph.getRecordNumber(), atomicField.get(), hsph));
                            try {
                                hsphItr.remove();
                                continue NEXT_HSPH_RECORD;
                            } catch (IllegalStateException e) {
                                // ignore
                            }
                        }
                    }
                }

            }




        }


    }

    private static class RecordMeta {
        long recordNo;
        String value;

        public RecordMeta(long recordNo, String value) {
            this.recordNo = recordNo;
            this.value = value.intern();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            RecordMeta that = (RecordMeta) o;
            return value == that.value;
        }

        @Override
        public int hashCode() {
            return Objects.hash(value);
        }
    }

    static Optional<String> internValue(int field, CSVRecord record) {
        if (record.get(field) != null && record.get(field).trim().length() > 0) {
            return Optional.of(record.get(field).trim().intern());
        }

        return Optional.empty();
    }

    static boolean valueEquals(String value, int field, CSVRecord record) {
        Objects.requireNonNull(value);
        return internValue(field, record).isPresent() && value == internValue(field, record).get();
    }

}
