// Copyright 2021 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.firebase.firestore.model;

import static com.google.firebase.firestore.model.FieldIndex.SEMANTIC_COMPARATOR;
import static com.google.firebase.firestore.testutil.TestUtil.fieldIndex;
import static com.google.firebase.firestore.testutil.TestUtil.version;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class FieldIndexTest {

  @Test
  public void comparatorIncludesCollectionGroup() {
    FieldIndex indexOriginal = fieldIndex("collA");
    FieldIndex indexSame = fieldIndex("collA");
    FieldIndex indexDifferent = fieldIndex("collB");
    assertEquals(0, SEMANTIC_COMPARATOR.compare(indexOriginal, indexSame));
    assertEquals(-1, SEMANTIC_COMPARATOR.compare(indexOriginal, indexDifferent));
  }

  @Test
  public void comparatorIgnoresIndexId() {
    FieldIndex indexOriginal = fieldIndex("collA", 1, SnapshotVersion.NONE);
    FieldIndex indexSame = fieldIndex("collA", 1, SnapshotVersion.NONE);
    FieldIndex indexDifferent = fieldIndex("collA", 2, SnapshotVersion.NONE);
    assertEquals(0, SEMANTIC_COMPARATOR.compare(indexOriginal, indexSame));
    assertEquals(0, SEMANTIC_COMPARATOR.compare(indexOriginal, indexDifferent));
  }

  @Test
  public void comparatorIgnoreUpdateTime() {
    FieldIndex indexOriginal = fieldIndex("collA", 1, version(1));
    FieldIndex indexSame = fieldIndex("collA", 1, version(1));
    FieldIndex indexDifferent = fieldIndex("collA", 1, version(2));
    assertEquals(0, SEMANTIC_COMPARATOR.compare(indexOriginal, indexSame));
    assertEquals(0, SEMANTIC_COMPARATOR.compare(indexOriginal, indexDifferent));
  }

  @Test
  public void comparatorIncludesFieldName() {
    FieldIndex indexOriginal = fieldIndex("collA", "a", FieldIndex.Segment.Kind.ASCENDING);
    FieldIndex indexSame = fieldIndex("collA", "a", FieldIndex.Segment.Kind.ASCENDING);
    FieldIndex indexDifferent = fieldIndex("collA", "b", FieldIndex.Segment.Kind.ASCENDING);
    assertEquals(0, SEMANTIC_COMPARATOR.compare(indexOriginal, indexSame));
    assertEquals(-1, SEMANTIC_COMPARATOR.compare(indexOriginal, indexDifferent));
  }

  @Test
  public void comparatorIncludesSegmentKind() {
    FieldIndex indexOriginal = fieldIndex("collA", "a", FieldIndex.Segment.Kind.ASCENDING);
    FieldIndex indexSame = fieldIndex("collA", "a", FieldIndex.Segment.Kind.ASCENDING);
    FieldIndex indexDifferent = fieldIndex("collA", "a", FieldIndex.Segment.Kind.DESCENDING);
    assertEquals(0, SEMANTIC_COMPARATOR.compare(indexOriginal, indexSame));
    assertEquals(-1, SEMANTIC_COMPARATOR.compare(indexOriginal, indexDifferent));
  }

  @Test
  public void comparatorIncludesSegmentsLength() {
    FieldIndex indexOriginal = fieldIndex("collA", "a", FieldIndex.Segment.Kind.ASCENDING);
    FieldIndex indexSame = fieldIndex("collA", "a", FieldIndex.Segment.Kind.ASCENDING);
    FieldIndex indexDifferent =
        fieldIndex(
            "collA",
            "a",
            FieldIndex.Segment.Kind.ASCENDING,
            "b",
            FieldIndex.Segment.Kind.ASCENDING);
    assertEquals(0, SEMANTIC_COMPARATOR.compare(indexOriginal, indexSame));
    assertEquals(-1, SEMANTIC_COMPARATOR.compare(indexOriginal, indexDifferent));
  }
}
