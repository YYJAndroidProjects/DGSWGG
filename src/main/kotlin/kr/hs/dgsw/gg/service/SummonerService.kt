package kr.hs.dgsw.gg.service

import kotlinx.coroutines.runBlocking
import kr.hs.dgsw.gg.api.objects.RetrofitObject.krRiotApi
import kr.hs.dgsw.gg.data.base.BaseDTO
import kr.hs.dgsw.gg.data.dto.SummonerDTO
import kr.hs.dgsw.gg.data.riot_response.RankResponse
import kr.hs.dgsw.gg.data.riot_response.SummonerResponse
import kr.hs.dgsw.gg.data.vo.toDTO
import kr.hs.dgsw.gg.repository.RankRepository
import kr.hs.dgsw.gg.repository.SummonerRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import retrofit2.HttpException
import javax.transaction.Transactional

@Service
class SummonerService(
    private val summonerRepository: SummonerRepository,
    private val rankRepository: RankRepository
) {
    fun getSummonerByName(summonerName: String): BaseDTO<SummonerDTO> {
<<<<<<< Updated upstream
        val summonerOpt = summonerRepository.getSummonerByName(summonerName)
        val summonerDTO =
            if (summonerOpt.isEmpty)
                getSummonerInfoFromRiotApi(summonerName).toDTO()
            else
                summonerOpt.get().toDTO()
=======
        val summonerDTO = summonerRepository.getSummonerBySummonerName(summonerName).orElseThrow {
            throw ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "404 NOT FOUND"
            )
        }.toDTO()
>>>>>>> Stashed changes

        return BaseDTO(HttpStatus.OK.value(), "성공", summonerDTO)
    }

    @Transactional
    fun postRefreshSummonerInfo(summonerName: String, name: String, grade: Int, klass: Int, number: Int): BaseDTO<Nothing?> {
        val summonerResponse = getSummonerInfoFromRiotApi(summonerName)
        val rankList = getRankBySummonerIdFromRiotApi(summonerResponse.id)
        summonerRepository.save(summonerResponse.toVO(name, grade, klass, number))
        rankRepository.deleteBySummonerName(summonerName)
        rankRepository.saveAll(rankList.map { it.toVO() })
        return BaseDTO(HttpStatus.OK.value(), "성공", null)
    }

    private fun getSummonerInfoFromRiotApi(summonerName: String): SummonerResponse {
        return runBlocking {
            try {
                krRiotApi.getSummonerByName(summonerName)
            } catch (e: HttpException) {
                throw ResponseStatusException(
                    HttpStatus.valueOf(e.code()),
                    e.message(),
                )
            }
        }
    }

    private fun getRankBySummonerIdFromRiotApi(summonerId: String): List<RankResponse> {
        return runBlocking {
            try {
                krRiotApi.getRankBySummonerId(summonerId)
            } catch (e: HttpException) {
                throw ResponseStatusException(
                    HttpStatus.valueOf(e.code()),
                    e.message(),
                )
            }
        }
    }
}