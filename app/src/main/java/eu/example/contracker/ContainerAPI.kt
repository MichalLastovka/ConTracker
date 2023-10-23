package eu.example.contracker

import ContainerData1
import retrofit2.Response

interface ContainerAPI {
    fun getContainer(): Response<ContainerData1>
}