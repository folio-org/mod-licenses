package org.olf

import grails.gorm.transactions.Transactional

/**
 * This service works at the module level, it's often called without a tenant context.
 */
@Transactional
public class LicenseHousekeepingService {

  def EndDateSemanticsCleanup

  public void triggerHousekeeping() {
    EndDateSemanticsCleanup.checkUnsetValues();
  }
}
