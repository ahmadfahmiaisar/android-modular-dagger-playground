package io.fajarca.project.login.data.repository

import io.fajarca.project.base.di.scope.ModuleScope
import io.fajarca.project.login.data.service.LoginService
import io.fajarca.project.login.data.mapper.UserMapper
import io.fajarca.project.login.domain.entity.User
import javax.inject.Inject
import io.fajarca.project.base.Either
import io.fajarca.project.base.abstraction.ApiClient
import io.fajarca.project.login.domain.repository.UserRepository

@ModuleScope
class UserRepositoryImpl @Inject constructor(
    private val mapper: UserMapper,
    private val apiClient : ApiClient,
    private val loginService: LoginService
) : UserRepository {

    override suspend fun getUsers(): Either<List<User>> {
        val apiResult = apiClient.call { loginService.getUsers() }
        return when (apiResult) {
            is Either.Success -> Either.Success(mapper.map(apiResult.data))
            is Either.Error -> Either.Error(apiResult.cause)
        }
    }

}