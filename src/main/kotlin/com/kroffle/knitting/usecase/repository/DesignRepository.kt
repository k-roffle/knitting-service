package com.kroffle.knitting.usecase.repository

import com.kroffle.knitting.usecase.design.DesignService
import com.kroffle.knitting.usecase.draftdesign.DraftDesignService
import com.kroffle.knitting.usecase.summary.ProfileSummaryService

interface DesignRepository :
    DesignService.DesignRepository,
    DraftDesignService.DesignRepository,
    ProfileSummaryService.DesignRepository
