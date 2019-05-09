package org.olf.licenses

import grails.gorm.MultiTenant

/**
 * A license link defines an edge on a graph of resources
 */
class LicenseLink implements MultiTenant<LicenseLink> {

  // Internal ID
  String id

  // What is the kind of object on the other end of the link. The UI will need this to work out how to mint
  // a URL the user can click on. In a semantic web environment, we would just use a URL, but here we have services
  // with resources that have IDs. The linkType enables us to know how to resolve the related object.
  // THe first part of the "Object" of our triple
  String linkType

  // the actual foreign key we want to link with. The second part of the "Object" for our triple
  String linkId

  // An optional label for the edge - describes the relatioship, and might be used for display to a user
  String linkLabel

  // The relationship - an optional field that defines the predicate we are using for this triple. The predicate for our triple
  String relation

  // The direction - sometimes it might be neccessary to have directionality in relationships ("In" or "Out"bound).
  // This is necessary because some remote objects might not allow Subject-Predicate-Object FROM their local datastore, so we will be
  // using this resource to describe an inbound link. This is a slight bastardisation of the directed graph, but it will have to do.
  // Having a direction allows this class to store assertions where the subject is an object in a remote system.
  String direction

  // The licenes the relationshiop relates to - the "Subject" of our triple.
  static belongsTo = [
    owner:License
  ]

  static constraints = {
     linkType(nullable:false, blank:false)
       linkId(nullable:false, blank:false)
    linkLabel(nullable:true, blank:false)
     relation(nullable:true, blank:false)
    direction(nullable:true, blank:false)
        owner(nullable:false, blank:false)
  }

  static mapping = {
             id column: 'll_id', generator: 'uuid2', length:36
       linkType column: 'll_type'
         linkId column: 'll_remote_id'
      linkLabel column: 'll_label'
          owner column: 'll_owning_license_fk'
       relation column: 'll_relation'
  }

}
