/*
 * Copyright (c) 2018, Edmund Noble
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors
 *    may be used to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package testz

import extras._

object ExtrasSuite {
  def tests[T](harness: Harness[T]): T = {
    import harness._
    namedSection("document harness")(
      test("entire harness") { () =>
        val docHarness = DocHarness.make
        val buf = new scala.collection.mutable.ListBuffer[String]()
        import docHarness.{namedSection => dNamedSection, section => dSection, test => dTest}
        dNamedSection("outer named section")(
          dNamedSection("first inner named section")(
            dTest("first test inside of first inner named section")(() => ???),
            dTest("second test inside of first inner named section")(() => ???)
          ),
          dNamedSection("second inner named section")(
            dTest("first test inside of second inner section")(() => ???),
            dSection(dTest("first test inside of section inside second inner named section")(() => ???)),
          )
        )("  ", buf)
        assert(buf.result() == List(
          "    [outer named section]",
          "      [first inner named section]",
          "        first test inside of first inner named section",
          "        second test inside of first inner named section",
          "      [second inner named section]",
          "        first test inside of second inner section",
          "          first test inside of section inside second inner named section"))
      }
    )
  }
}
