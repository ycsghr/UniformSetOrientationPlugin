import org.gradle.api.Plugin
import org.gradle.api.Project

class OrientationPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {

        def extension = project.extensions.create('testSet', TestsetOrientationPlugin)

        project.afterEvaluate {
            project.plugins.withId('com.android.application') {

                project.tasks.getByName("processDebugManifest") {

                    project.android.applicationVariants.all { variant ->
                        variant.outputs.each { output ->
                            output.getProcessManifestProvider().get().doLast {
                                //合并时Mainfest地址
                                String manifestPathDirectory = manifestOutputDirectory.get()
                                String manifestPath = manifestPathDirectory + '/AndroidManifest.xml'
                                println('manifestPath=' + manifestPath)
                                String manifestContent = new File(manifestPath).getText('UTF-8')
                                println('manifestContent=' + manifestContent)
                                def xml = new XmlParser().parseText(manifestContent)
                                println('xml=' + xml)
                                def activitys = xml.application[0].activity
                                println('activitys=' + activitys)

                                activitys.each { activity ->
                                    println('activity=' + activity)
                                    def attrs = activity.attributes()
                                    def isAddScreenOrientation = true
                                    attrs.each { attr ->
                                        def key = attr.getKey()
                                        if ('name'.equals(key.localPart) && extension.dontSetActivityNames.contains(attr.getValue())) {
                                            isAddScreenOrientation = false

                                        } else if ('screenOrientation'.equals(key.localPart)) {
                                            isAddScreenOrientation = false
                                        }


                                    }
                                    println('isAddScreenOrientation=' + isAddScreenOrientation)
                                    if (isAddScreenOrientation) {
                                        attrs.put('android:screenOrientation', extension.screenOrientation)
                                    }

                                }
                                def serialize = groovy.xml.XmlUtil.serialize(xml)
                                new File(manifestPath).write(serialize)


                            }


                        }

                    }

                }


            }
        }
    }

}