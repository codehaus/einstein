/******************************************************************************
 *                                                                            *
 *                                                                            *
 *     All works are (C) 2008- Mangala Solutions Ltd and Paremus Ltd.         *
 *                                                                            *
 *     Jointly liicensed to Mangala Solutions and Paremus under one           *
 *     or more contributor license agreements.  See the NOTICE file           *
 *     distributed with this work for additional information                  *
 *     regarding copyright ownership.  Mangala Solutions and Paremus          *
 *     licenses this file to you under the Apache License, Version            *
 *     2.0 (the "License"); you may not use this file except in               *
 *     compliance with the License.  You may obtain a copy of the             *
 *     License at                                                             *
 *                                                                            *
 *             http://www.apache.org/licenses/LICENSE-2.0                     *
 *                                                                            *
 *     Unless required by applicable law or agreed to in writing,             *
 *     software distributed under the License is distributed on an            *
 *     "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY                 *
 *     KIND, either express or implied.  See the License for the              *
 *     specific language governing permissions and limitations                *
 *     under the License.                                                     *
 *                                                                            *
 ******************************************************************************/

package org.cauldron.einstein.api.assembly.instruction.annotation;

/**
 * These are the different types of operator supported by Einstein, operators can be more than one type. It's perfectly
 * reasonable for an operator to support any combination of these types (except the reversed types and their un-reversed
 * partners).
 *
 * @author Neil Ellis
 */
public enum OperatorType {
    /**
     * This refers to an operator which can work without brackets on multiple expressions. The N-ary operators can
     * parallelize execution of their arguments and then operate on the result. They exist to encourage parallelism.
     * E.g.: <br/>  </br/> <code> (read "text:1") & (read "text:2") & (read "text:3") </code>
     */
    N_ARY,
    /**
     * This refers to an operator that supports basic binary operations, e.g.:
     * <br/> <code>
     * ( read "text:1" ) >
     * "console:Out ..."
     * <p/>
     * </code>
     */
    BINARY,
    /**
     * Unary operators pass the expression to their instruction as the first argument. A typical unary expression is:
     * <br/> <code> -- "java:org.me.MyStack" </code>
     */
    UNARY,

    /**
     * Ternary operators are similar to Java's single terneray operator ? :. The main difference is that Einstein allows
     * the first part of the syntax to differ; the second part is always a ':'. E.g.:
     * <p/>
     * <pre>
     * (read ...) % "xpath://thing" : {
     *               do things
     * }
     * </pre>
     */
    TERNARY,
    /**
     * Unary reversed operators are ones which evaluate their expression first, and pass the result to the
     * instruction.E.g. <br/>
     * <pre>
     * ~[read "text:1", read "text:2"];
     * </pre>
     * <p/>
     * </pre>
     *
     * This reads two values into a tuple and then the result is merged together to form a single message.
     */
    UNARY_REVERSED,

    /**
     * This signifies an operator has a valid meaning on it's own. To avoid confusion the syntax is  "(" <operator> ")".
     * This is a useful shorthand, but use wisely as it can really make code look confusing, like Perl.
     */
    OPERATOR_ONLY,


    /**
     * Binary reversed means that the right hand side of the operator is actually evaluated before the operation is performed.
     * The best example is the read operator "<<" so:
     * <p><pre><code>
     * write "console:Result" << "text:1";
     * </code></pre></p>
     * reads from the text provider to obtain the string "1" and then this becomes the input for the left hand side.
     *
     */
    BINARY_REVERSED
}
