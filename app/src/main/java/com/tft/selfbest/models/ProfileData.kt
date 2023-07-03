package com.tft.selfbest.models


import com.google.gson.annotations.SerializedName

//data class ProfileData(
//
//    @SerializedName("Contact")
//    val contact: String?,
//    @SerializedName("accountabilitypartner")
//    val accountabilitypartner: Boolean?,
//    @SerializedName("CreatedAt")
//    val createdAt: String?,
//    @SerializedName("Dob")
//    val dob: String?,
//    @SerializedName("Email")
//    val email: String?,
//    @SerializedName("ExistingGoals")
//    val existingGoals: List<String>?,
//    @SerializedName("ExtensionUsedAt")
//    val extensionUsedAt: String?,
//    @SerializedName("FirstName")
//    val firstName: String?,
//    @SerializedName("Gender")
//    var gender: String?,
//    @SerializedName("Id")
//    val id: Int?,
//    @SerializedName("Image")
//    val image: String?,
//    @SerializedName("ImportConnectionsLinkedIn")
//    val importConnectionsLinkedIn: Boolean?,
//    @SerializedName("ImportGoogleCalendar")
//    val importGoogleCalendar: Boolean?,
//    @SerializedName("ImportOutlookCalendar")
//    val importOutlookCalendar: Boolean?,
//    @SerializedName("Interests")
//    val interests: List<String>?,
//    @SerializedName("is_org_admin")
//    val isOrgAdmin: Boolean?,
//    @SerializedName("Isadmin")
//    val isadmin: Boolean?,
//    @SerializedName("Isapproved")
//    val isapproved: Int?,
//    @SerializedName("JiraId")
//    val jiraId: String?,
//    @SerializedName("LastName")
//    val lastName: String?,
//    @SerializedName("Lastlogin")
//    val lastlogin: String?,
//    @SerializedName("Lastloginworkinghour")
//    val lastloginworkinghour: Boolean?,
//    @SerializedName("Learning")
//    val learning: List<Learning>?,
//    @SerializedName("linkedin_profile_url")
//    val linkedinProfileUrl: String?,
//    @SerializedName("NoOfExperience")
//    val noOfExperience: Number?,
//    @SerializedName("Occupation")
//    val occupation: String?,
//    @SerializedName("Password")
//    val password: String?,
//    @SerializedName("PersonalityType")
//    val personalityType: String?,
//    @SerializedName("RejectedCount")
//    val rejectedCount: Int?,
//    @SerializedName("role")
//    val role: String?,
//    @SerializedName("Skillenhanced")
//    val skillenhanced: Int?,
//    @SerializedName("Skills")
//    var skills: Any?,
//    @SerializedName("PendingSkills")
//    var pendingskills: Any?,
//    @SerializedName("TimeZone")
//    val timeZone: String?,
//    @SerializedName("TypicalProcrastinatorTrigger")
//    val typicalProcrastinatorTrigger: List<String>?,
//    @SerializedName("UpdatedAt")
//    val updatedAt: String?,
//    @SerializedName("Working")
//    val working: List<ProfileWorkingData>,
//    @SerializedName("OrganisationName")
//    val organisationName: String,
//    @SerializedName("CustomPersonality")
//    val customPersonality: List<String>
//)


data class ProfileData(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("Email")
    val email: String?,
    @SerializedName("FirstName")
    val firstName: String?,
    @SerializedName("LastName")
    val lastName: String?,
    @SerializedName("Gender")
    var gender: String?,
    @SerializedName("Contact")
    val contact: String?,
    @SerializedName("Password")
    val password: String?,
    @SerializedName("Dob")
    val dob: String?,
    @SerializedName("Image")
    val image: String?,
    @SerializedName("PersonalityType")
    val personalityType: String?,
    @SerializedName("Occupation")
    val occupation: String?,
    @SerializedName("JiraId")
    val jiraId: String?,
    @SerializedName("TimeZone")
    val timeZone: String?,
    @SerializedName("NoOfExperience")
    val noOfExperience: Number?,
    @SerializedName("ImportGoogleCalendar")
    val importGoogleCalendar: Boolean?,
    @SerializedName("ImportOutlookCalendar")
    val importOutlookCalendar: Boolean?,
    @SerializedName("CreatedAt")
    val createdAt: String?,
    @SerializedName("UpdatedAt")
    val updatedAt: String?,
    @SerializedName("Lastlogin")
    val lastlogin: String?,
    @SerializedName("Skillenhanced")
    val skillenhanced: Int?,
    @SerializedName("Isapproved")
    val isapproved: Int?,
    @SerializedName("Isadmin")
    val isadmin: Boolean?,
    @SerializedName("RejectedCount")
    val rejectedCount: Int?,
    @SerializedName("AccountabilityPartnerEmail")
    val accountabilityPartnerEmail: String?,
    @SerializedName("ShowLeaderBoard")
    val showLeaderBoard: Boolean?,
    @SerializedName("role")
    val role: String?,
    @SerializedName("linkedin_profile_url")
    val linkedinProfileUrl: String?,
    @SerializedName("ExtensionUsedAt")
    val extensionUsedAt: String?,
    @SerializedName("new_user")
    val newUser: Boolean?,
    @SerializedName("AccountabilityPartner")
    val accountabilityPartner: Boolean?,
    @SerializedName("Deleted")
    val deleted: Boolean?,
    @SerializedName("Resume")
    val resume: String?,
    @SerializedName("Name")
    val name: String?,
    @SerializedName("Platform")
    val platform: String?,
    @SerializedName("DeleteAccountStatus")
    val deleteAccountStatus: String?,
    @SerializedName("DeleteAccountDataStatus")
    val deleteAccountDataStatus: String?,
    @SerializedName("DeactivateAccountStatus")
    val deactivateAccountStatus: String?,
    @SerializedName("last_time_updated_skill")
    val lastTimeUpdatedSkill: String?,
    @SerializedName("IsInstalledWindowsapp")
    val isInstalledWindowsapp: Boolean?,
    @SerializedName("IsInstalledMacapp")
    val isInstalledMacapp: Boolean?,
    @SerializedName("IsDownloadedWindowsapp")
    val isDownloadedWindowsapp: Boolean?,
    @SerializedName("IsDownloadedMacapp")
    val isDownloadedMacapp: Boolean?,
    @SerializedName("IsDownloadedChromeExtension")
    val isDownloadedChromeExtension: Boolean?,
    @SerializedName("IsInstalledChromeExtension")
    val isInstalledChromeExtension: Boolean?,
    @SerializedName("is_org_admin")
    val isOrgAdmin: Boolean?,
    @SerializedName("CustomPersonality")
    val customPersonality: List<String>?,
    @SerializedName("Skills")
    var skills: Any?,
    @SerializedName("Working")
    val working: List<ProfileWorkingData>,
//    @SerializedName("Availability")
//    val availability: String?,
    @SerializedName("OrganisationName")
    val organisationName: String?,
    @SerializedName("OrganisationCalendar")
    val organisationCalendar: String,
    @SerializedName("ShowWorkDen")
    val showWorkDen: Boolean?,
    @SerializedName("ShowSolutionPoint")
    val showSolutionPoint: Boolean?,
    @SerializedName("SlackWorkspaceID")
    val slackWorkspaceID: String?,
    @SerializedName("OrganisationID")
    val organisationID: Int?,
    @SerializedName("PendingSkills")
    var pendingskills: Any?,
    @SerializedName("BotChoice")
    val botChoice: String?,
    @SerializedName("Level")
    val level: Any?,
    @SerializedName("Simplifypath")
    val simplifypath: Boolean?,
    @SerializedName("Integratedtools") //which to show
    val integratedTools: IntegratedTools?,
    @SerializedName("DeviceConnected") //connected or not
    val deviceConnected: DeviceConnected?
)

data class IntegratedTools(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("OrganisationId")
    val organisationId: Int?,
    @SerializedName("GoogleCalendar")
    val googleCalendar: Boolean?,
    @SerializedName("MicrosoftCalendar")
    val microsoftCalendar: Boolean?,
    @SerializedName("Jira")
    val jira: Boolean?,
    @SerializedName("Gmail")
    val gmail: Boolean?,
    @SerializedName("Salesforce")
    val salesforce: Boolean?,
    @SerializedName("Keka")
    val keka: Boolean?,
    @SerializedName("Freshwork")
    val freshwork: Boolean?,
)

data class DeviceConnected(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("UserId")
    val userId: Int?,
    @SerializedName("GoogleCalendar")
    val googleCalendar: Boolean?,
    @SerializedName("MicrosoftCalendar")
    val microsoftCalendar: Boolean?,
    @SerializedName("Jira")
    val jira: Boolean?,
    @SerializedName("Gmail")
    val gmail: Boolean?,
    @SerializedName("Salesforce")
    val salesforce: Boolean?,
    @SerializedName("Keka")
    val keka: Boolean?,
    @SerializedName("Freshwork")
    val freshwork: Boolean?,
)