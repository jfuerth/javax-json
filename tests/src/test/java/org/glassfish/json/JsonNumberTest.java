/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2012 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package org.glassfish.json;

import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;

import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonNumber;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.json.JsonWriter;

import junit.framework.TestCase;

/**
 * @author Jitendra Kotamraju
 */
public class JsonNumberTest extends TestCase {
    public JsonNumberTest(String testName) {
        super(testName);
    }

    public void testFloating() throws Exception {
        JsonArray array1 = new JsonArrayBuilder().add(10.4).build();
        JsonReader reader = new JsonReader(new StringReader("[10.4]"));
        JsonArray array2 = reader.readArray();

        assertEquals(array1.get(0), array2.get(0));
        assertEquals(array1, array2);
    }

    public void testBigDecimal() throws Exception {
        JsonArray array1 = new JsonArrayBuilder().add(new BigDecimal("10.4")).build();
        JsonReader reader = new JsonReader(new StringReader("[10.4]"));
        JsonArray array2 = reader.readArray();

        assertEquals(array1.get(0), array2.get(0));
        assertEquals(array1, array2);
    }

    public void testIntNumberType() throws Exception {
        JsonArray array1 = new JsonArrayBuilder()
                .add(Integer.MIN_VALUE)
                .add(Integer.MAX_VALUE)
                .add(Integer.MIN_VALUE + 1)
                .add(Integer.MAX_VALUE - 1)
                .add(12)
                .add(12l)
                .add(new BigInteger("0"))
                .build();
        testNumberType(array1, JsonNumber.NumberType.INTEGER);

        StringReader sr = new StringReader("[" +
                "-2147483648, " +
                "2147483647, " +
                "-2147483647, " +
                "2147483646, " +
                "12, " +
                "12, " +
                "0 " +
                "]");
        JsonReader reader = new JsonReader(sr);
        JsonArray array2 = reader.readArray();
        reader.close();
        testNumberType(array2, JsonNumber.NumberType.INTEGER);

        assertEquals(array1, array2);
    }

    private void testNumberType(JsonArray array, JsonNumber.NumberType numberType) {
        for (JsonValue value : array) {
            assertEquals(numberType, ((JsonNumber) value).getNumberType());
        }
    }

    public void testLongNumberType() throws Exception {
        JsonArray array1 = new JsonArrayBuilder()
                .add(Long.MIN_VALUE)
                .add(Long.MAX_VALUE)
                .add(Long.MIN_VALUE + 1)
                .add(Long.MAX_VALUE - 1)
                .add((long) Integer.MIN_VALUE - 1)
                .add((long) Integer.MAX_VALUE + 1)
                .build();
        testNumberType(array1, JsonNumber.NumberType.INTEGER);

        StringReader sr = new StringReader("[" +
                "-9223372036854775808, " +
                "9223372036854775807, " +
                "-9223372036854775807, " +
                "9223372036854775806, " +
                "-2147483649, " +
                "2147483648 " +
                "]");
        JsonReader reader = new JsonReader(sr);
        JsonArray array2 = reader.readArray();
        reader.close();
        testNumberType(array2, JsonNumber.NumberType.INTEGER);

        assertEquals(array1, array2);
    }


//    public void testBigIntegerNumberType() throws Exception {
//        JsonArray array1 = new JsonBuilder()
//            .startArray()
//                .add(new BigInteger("-9223372036854775809"))
//                .add(new BigInteger("9223372036854775808"))
//                .add(new BigInteger("012345678901234567890"))
//            .end()
//        .build();
//        testNumberType(array1, JsonNumber.NumberType.BIG_INTEGER);
//
//        StringReader sr = new StringReader("[" +
//            "-9223372036854775809, " +
//            "9223372036854775808, " +
//            "12345678901234567890 " +
//        "]");
//        JsonReader reader = new JsonReader(sr);
//        JsonArray array2 = reader.readArray();
//        reader.close();
//        testNumberType(array2, JsonNumber.NumberType.BIG_INTEGER);
//
//        assertEquals(array1, array2);
//    }

    public void testBigDecimalNumberType() throws Exception {
        JsonArray array1 = new JsonArrayBuilder()
                .add(12d)
                .add(12.0d)
                .add(12.1d)
                .add(Double.MIN_VALUE)
                .add(Double.MAX_VALUE)
                .build();
        testNumberType(array1, JsonNumber.NumberType.DECIMAL);

        StringReader sr = new StringReader("[" +
                "12.0, " +
                "12.0, " +
                "12.1, " +
                "4.9E-324, " +
                "1.7976931348623157E+308 " +
                "]");
        JsonReader reader = new JsonReader(sr);
        JsonArray array2 = reader.readArray();
        reader.close();
        testNumberType(array2, JsonNumber.NumberType.DECIMAL);

        assertEquals(array1, array2);
    }

    public void testMinMax() throws Exception {
        JsonArray expected = new JsonArrayBuilder()
                .add(Integer.MIN_VALUE)
                .add(Integer.MAX_VALUE)
                .add(Long.MIN_VALUE)
                .add(Long.MAX_VALUE)
                .add(Double.MIN_VALUE)
                .add(Double.MAX_VALUE)
                .build();

        StringWriter sw = new StringWriter();
        JsonWriter writer = new JsonWriter(sw);
        writer.writeArray(expected);
        writer.close();

        JsonReader reader = new JsonReader(new StringReader(sw.toString()));
        JsonArray actual = reader.readArray();
        reader.close();

        assertEquals(expected, actual);
    }

    public void testLeadingZeroes() {
        JsonArray array = new JsonArrayBuilder()
                .add(0012.1d) // FIXME these zeroes are removed by the Java compiler
                .build();

        StringWriter sw = new StringWriter();
        JsonWriter jw = new JsonWriter(sw);
        jw.write(array);
        jw.close();

        assertEquals("[12.1]", sw.toString());
    }

    public void testBigIntegerExact() {
        try {
            JsonArray array = new JsonArrayBuilder().add(12345.12345).build();
            array.getValue(0, JsonNumber.class).getBigIntegerValueExact();
            fail("Expected Arithmetic exception");
        } catch (ArithmeticException expected) {
            // no-op
        }
    }

    public void testPreserveTrailingZeroes() {
        JsonReader reader = new JsonReader(new StringReader("[10.400]"));
        JsonNumber num = reader.readArray().getValue(0, JsonNumber.class);
        reader.close();

        BigDecimal bigDecimalValue = num.getBigDecimalValue();
        assertEquals(new BigDecimal("10.400"), bigDecimalValue);
        assertEquals("10.400", num.toString());
    }

    public void testPreserveNegativeZero() {
        JsonReader reader = new JsonReader(new StringReader("[-0.0]"));
        JsonNumber num = reader.readArray().getValue(0, JsonNumber.class);
        reader.close();
System.out.println(new BigDecimal("-0.0"));
        assertEquals("-0.0", num.toString());
    }

    public void testHashCodePositiveInteger() {
        JsonReader reader = new JsonReader(new StringReader("[1234]"));
        JsonNumber num = reader.readArray().getValue(0, JsonNumber.class);
        reader.close();

        assertEquals("1234".hashCode(), num.hashCode());
    }

    public void testHashCodeNegativeInteger() {
        JsonReader reader = new JsonReader(new StringReader("[-1234]"));
        JsonNumber num = reader.readArray().getValue(0, JsonNumber.class);
        reader.close();

        assertEquals("-1234".hashCode(), num.hashCode());
    }

    public void testHashCodeIntegerWithTrailingZeroes() {
        JsonReader reader = new JsonReader(new StringReader("[1234000]"));
        JsonNumber num = reader.readArray().getValue(0, JsonNumber.class);
        reader.close();

        assertEquals("1.234E+6".hashCode(), num.hashCode());
    }

    // test disabled until leading zeroes case is fixed in parser
//    public void testHashCodeIntegerWithLeadingZeroes() {
//        JsonReader reader = new JsonReader(new StringReader("[00001234]"));
//        JsonNumber num = reader.readArray().getValue(0, JsonNumber.class);
//        reader.close();
//
//        assertEquals("1234".hashCode(), num.hashCode());
//    }

    public void testHashCodeDecimalWithTrailingZeroes() {
        JsonReader reader = new JsonReader(new StringReader("[12.34000]"));
        JsonNumber num = reader.readArray().getValue(0, JsonNumber.class);
        reader.close();

        assertEquals("12.34".hashCode(), num.hashCode());
    }

    public void testHashCodeDecimalLessThanOneWithTrailingZeroes() {
        JsonReader reader = new JsonReader(new StringReader("[0.1234000]"));
        JsonNumber num = reader.readArray().getValue(0, JsonNumber.class);
        reader.close();

        assertEquals("0.1234".hashCode(), num.hashCode());
    }

    public void testHashCodeDecimalLessThanOneWithLeadingZeroes() {
        JsonReader reader = new JsonReader(new StringReader("[0.00000001234]"));
        JsonNumber num = reader.readArray().getValue(0, JsonNumber.class);
        reader.close();

        assertEquals("1.234E-8".hashCode(), num.hashCode());
    }

    public void testHashCodeZero() {
        JsonReader reader = new JsonReader(new StringReader("[0]"));
        JsonNumber num = reader.readArray().getValue(0, JsonNumber.class);
        reader.close();

        assertEquals("0".hashCode(), num.hashCode());
    }

    public void testHashCodeZeroPointZero() {
        JsonReader reader = new JsonReader(new StringReader("[0.0]"));
        JsonNumber num = reader.readArray().getValue(0, JsonNumber.class);
        reader.close();

        assertEquals("0".hashCode(), num.hashCode());
    }

    public void testHashCodeNegativeZeroPointZero() {
        JsonReader reader = new JsonReader(new StringReader("[-0.0]"));
        JsonNumber num = reader.readArray().getValue(0, JsonNumber.class);
        reader.close();

        assertEquals("0".hashCode(), num.hashCode());
    }

    public void testHashCodeTen() {
        JsonReader reader = new JsonReader(new StringReader("[10]"));
        JsonNumber num = reader.readArray().getValue(0, JsonNumber.class);
        reader.close();

        assertEquals("1E+1".hashCode(), num.hashCode());
    }

    public void testHashCodeTenPointZero() {
        JsonReader reader = new JsonReader(new StringReader("[10.0]"));
        JsonNumber num = reader.readArray().getValue(0, JsonNumber.class);
        reader.close();

        assertEquals("1E+1".hashCode(), num.hashCode());
    }

    /**
     * BigDecimal.toString() never uses scientific notation for
     * trailing-zero-stripped values bigger than 1E-6.
     */
    public void testHashCodeLargeNumber() {
        JsonReader reader = new JsonReader(new StringReader("[999999999]"));
        JsonNumber num = reader.readArray().getValue(0, JsonNumber.class);
        reader.close();

        assertEquals("999999999".hashCode(), num.hashCode());
    }

    /**
     * BigDecimal.toString() always uses scientific notation for numbers whose
     * absolute value is less than 1E-6.
     */
    public void testHashCodeNumberBelowScientificNotationThreshold() {
        JsonReader reader = new JsonReader(new StringReader("[0.000000999999999]"));
        JsonNumber num = reader.readArray().getValue(0, JsonNumber.class);
        reader.close();

        assertEquals("9.99999999E-7".hashCode(), num.hashCode());
    }

    /**
     * BigDecimal.toString() never uses scientific notation for numbers whose
     * absolute value is between 1E-6 and 1.0.
     */
    public void testHashCodeNumberAboveScientificNotationThreshold() {
        JsonReader reader = new JsonReader(new StringReader("[0.00000999999999]"));
        JsonNumber num = reader.readArray().getValue(0, JsonNumber.class);
        reader.close();

        assertEquals("0.00000999999999".hashCode(), num.hashCode());
    }

    public void testZeroEqualsZeroPointZero() {
        JsonReader reader = new JsonReader(new StringReader("[0, 0.0]"));
        JsonArray array = reader.readArray();
        reader.close();
        JsonNumber num0 = array.getValue(0, JsonNumber.class);
        JsonNumber num1 = array.getValue(1, JsonNumber.class);

        assertEquals(num0, num1);
        assertEquals(num1, num0);
    }

    public void testTenEqualsTenPointZero() {
        JsonReader reader = new JsonReader(new StringReader("[10, 10.0]"));
        JsonArray array = reader.readArray();
        reader.close();
        JsonNumber num0 = array.getValue(0, JsonNumber.class);
        JsonNumber num1 = array.getValue(1, JsonNumber.class);

        assertEquals(num0, num1);
        assertEquals(num1, num0);
    }

    public void testEqualsDisregardsTrailingZeroes() {
        JsonReader reader = new JsonReader(new StringReader("[123.45600, 123.456]"));
        JsonArray array = reader.readArray();
        reader.close();
        JsonNumber num0 = array.getValue(0, JsonNumber.class);
        JsonNumber num1 = array.getValue(1, JsonNumber.class);

        assertEquals(num0, num1);
        assertEquals(num1, num0);
    }

}