package com.kroffle.knitting.usecase.repository

import com.kroffle.knitting.usecase.design.DesignService
import com.kroffle.knitting.usecase.draftdesign.DraftDesignService

interface DraftDesignRepository : DraftDesignService.DraftDesignRepository, DesignService.DraftDesignRepository
