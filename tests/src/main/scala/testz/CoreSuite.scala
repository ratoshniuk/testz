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

object CoreSuite {
  def tests[T](harness: Harness[T], combineUses: (T, T) => T): T = {
    import harness._
    combineUses(
      section("assert")(
        test("success") { () =>
          if (assert(true) eq Succeed()) Succeed()
          else Fail()
        },
        test("failure") { () =>
          if (assert(false) eq Fail()) Succeed()
          else Fail()
        }
      ),
      section("result methods")(
        test("Result.combine") { () =>
          val data = List(
            (Succeed(), Succeed(), Succeed()),
            (Succeed(), Fail(), Fail()),
            (Fail(), Succeed(), Fail()),
            (Fail(), Fail(), Fail()),
          )
          data.foldLeft(Succeed()) {
            case (r, (i1, i2, o)) =>
              if (r == Fail())
                r
              else
                assert(Result.combine(i1, i2) == o)
          }
        },
        section("Result#equals")(
          test("agrees with eq") { () =>
            val data = List(
              (Succeed(), Succeed()),
              (Succeed(), Fail()),
              (Fail(), Succeed()),
              (Fail(), Fail()),
            )
            data.map {
              case (i1, i2) =>
                assert((i1 eq i2) == (i1 == i2))
            }.reduce(Result.combine)
          },
          test("exhaustive") { () =>
            val data = List(
              (Succeed(), Succeed(), true),
              (Succeed(), Fail(), false),
              (Fail(), Succeed(), false),
              (Fail(), Fail(), true),
            )
            data.map {
              case (i1, i2, o) =>
                assert((i1 == i2) == o)
            }.reduce(Result.combine)
          },
        )
      )
    )
  }
}
