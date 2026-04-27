package com.walkmate.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.walkmate.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * 홈 화면 — 오늘 걸음수, 주간 통계, 그룹 미니 카드.
 *
 * Week 0 placeholder. Week 2-3 실제 구현:
 * - StepCounterService → SensorRepository → HomeViewModel collect
 * - DailyStepEntity 기반 주간 그래프 (MPAndroidChart)
 * - StepSourceResolver: HC 우선 → Sensor fallback (ADR-001)
 */
@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
