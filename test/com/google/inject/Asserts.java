/**
 * Copyright (C) 2008 Google Inc.
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


package com.google.inject;

import junit.framework.Assert;

import java.io.*;

/**
 * @author jessewilson@google.com (Jesse Wilson)
 */
public class Asserts {
  private Asserts() {}

  /**
   * Fails unless {@code expected.equals(actual)}, {@code
   * actual.equals(expected)} and their hash codes are equal. This is useful
   * for testing the equals method itself.
   */
  public static void assertEqualsBothWays(Object expected, Object actual) {
    Assert.assertNotNull(expected);
    Assert.assertNotNull(actual);
    Assert.assertTrue("expected.equals(actual)", expected.equals(actual));
    Assert.assertTrue("actual.equals(expected)", actual.equals(expected));
    Assert.assertEquals("hashCode", expected.hashCode(), actual.hashCode());
  }

  /**
   * Fails unless {@code text} includes {@code substring}.
   */
  public static void assertContains(String text, String substring) {
    Assert.assertTrue(String.format("Expected \"%s\" to contain substring \"%s\"",
        text, substring), text.contains(substring));
  }

  /**
   * Fails unless {@code object} doesn't equal itself when reserialized.
   */
  public static void assertEqualWhenReserialized(Object object)
      throws IOException {
    Object reserialized = reserialize(object);
    Assert.assertEquals(object, reserialized);
    Assert.assertEquals(object.hashCode(), reserialized.hashCode());
  }

  /**
   * Fails unless {@code object} has the same toString value when reserialized.
   */
  public static void assertSimilarWhenReserialized(Object object) throws IOException {
    Object reserialized = reserialize(object);
    Assert.assertEquals(object.toString(), reserialized.toString());
  }

  static Object reserialize(Object object) throws IOException {
    try {
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      new ObjectOutputStream(out).writeObject(object);
      ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
      return new ObjectInputStream(in).readObject();
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  public static void assertNotSerializable(Object object) throws IOException {
    try {
      reserialize(object);
      Assert.fail();
    } catch (NotSerializableException expected) {
    }
  }
}