package com.walkmate

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Sanity test — Week 0 placeholder.
 *
 * Week 2-7 진행 시 실제 ViewModel/Repository/UseCase 단위 테스트로 대체:
 * - StepAccumulatorTest (재부팅 시뮬, 자정 롤오버)
 * - StepSourceResolverTest (HC 우선 → Sensor fallback 전환)
 * - WalkSessionRepositoryTest (Polyline encode/decode)
 * - RankingViewModelTest (Top 10 정렬)
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }
}
