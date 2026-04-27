package com.walkmate.ui.ranking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.walkmate.databinding.FragmentRankingBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * 랭킹 화면 — 그룹 Top 10 리더보드.
 *
 * Week 0 placeholder. Week 7 실제 구현:
 * - GET /groups/:id/ranking?period=weekly (Top 10 limit)
 * - 1/2/3위 포디움 + 4-10위 리스트
 * - 본인 행 walkmate_ranking_self_highlight 강조
 */
@AndroidEntryPoint
class RankingFragment : Fragment() {

    private var _binding: FragmentRankingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRankingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
