# Guestbook App on GKE

Overview

## Description

## Demo

### Prerequisite
#### Google Cloud SDK
- [For MacOS](https://cloud.google.com/sdk/docs/quickstart-macos?hl=ja)

```
$ ./google-cloud-sdk/install.sh
```

#### GCP Login
```
$ gcloud auth login --project PROJECT_ID
```

#### Enabled GCP Service List
```
$ gcloud services list
```
```
NAME                                 TITLE
bigquery.googleapis.com              BigQuery API
bigquery.googleapis.com              BigQuery API
bigquerystorage.googleapis.com       BigQuery Storage API
bigtable.googleapis.com              Cloud Bigtable API
bigtableadmin.googleapis.com         Cloud Bigtable Admin API
cloudapis.googleapis.com             Google Cloud APIs
cloudbilling.googleapis.com          Cloud Billing API
clouddebugger.googleapis.com         Stackdriver Debugger API
cloudkms.googleapis.com              Cloud Key Management Service (KMS) API
cloudresourcemanager.googleapis.com  Cloud Resource Manager API
cloudtrace.googleapis.com            Stackdriver Trace API
compute.googleapis.com               Compute Engine API
container.googleapis.com             Kubernetes Engine API
containerregistry.googleapis.com     Container Registry API
datastore.googleapis.com             Cloud Datastore API
deploymentmanager.googleapis.com     Cloud Deployment Manager V2 API
dns.googleapis.com                   Cloud DNS API
file.googleapis.com                  Cloud Filestore API
firebaserules.googleapis.com         Firebase Rules API
firestore.googleapis.com             Cloud Firestore API
iam.googleapis.com                   Identity and Access Management (IAM) API
iamcredentials.googleapis.com        IAM Service Account Credentials API
iap.googleapis.com                   Cloud Identity-Aware Proxy API
logging.googleapis.com               Stackdriver Logging API
monitoring.googleapis.com            Stackdriver Monitoring API
oslogin.googleapis.com               Cloud OS Login API
pubsub.googleapis.com                Cloud Pub/Sub API
redis.googleapis.com                 Google Cloud Memorystore for Redis API
replicapool.googleapis.com           Compute Engine Instance Group Manager API
replicapoolupdater.googleapis.com    Compute Engine Instance Group Updater API
resourceviews.googleapis.com         Compute Engine Instance Groups API
servicemanagement.googleapis.com     Service Management API
serviceusage.googleapis.com          Service Usage API
sql-component.googleapis.com         Cloud SQL
sqladmin.googleapis.com              Cloud SQL Admin API
storage-api.googleapis.com           Google Cloud Storage JSON API
storage-component.googleapis.com     Cloud Storage
```

If `Container Registry API` and `Kubernetes Engine API` is not enabled, execute the following command;
```
$ gcloud services enable containerregistry.googleapis.com
$ gcloud services enable compute.googleapis.com container.googleapis.com
```

### GKE Cluster Lifecycle
#### Create GKE Cluster
`Usage: gcloud container clusters create NAME [optional flags]`

```
$ gcloud container clusters create spring-gs-cluster \
  --num-nodes 4 \
  --machine-type n1-standard-1 \
  --zone asia-northeast1-b \
  --scopes cloud-platform
```

#### Create Cluster Options

```
  optional flags may be  --accelerator | --additional-zones | --addons |
                         --async | --autoprovisioning-config-file |
                         --autoprovisioning-locations |
                         --autoprovisioning-scopes |
                         --autoprovisioning-service-account |
                         --cluster-ipv4-cidr | --cluster-secondary-range-name |
                         --cluster-version | --create-subnetwork |
                         --database-encryption-key |
                         --default-max-pods-per-node | --disk-size |
                         --disk-type | --enable-autoprovisioning |
                         --enable-autorepair | --enable-autoscaling |
                         --enable-autoupgrade | --enable-basic-auth |
                         --enable-binauthz | --enable-cloud-logging |
                         --enable-cloud-monitoring | --enable-cloud-run-alpha |
                         --enable-intra-node-visibility | --enable-ip-alias |
                         --enable-kubernetes-alpha |
                         --enable-legacy-authorization |
                         --enable-master-authorized-networks |
                         --enable-network-egress-metering |
                         --enable-network-policy | --enable-private-endpoint |
                         --enable-private-nodes |
                         --enable-resource-consumption-metering |
                         --enable-stackdriver-kubernetes | --enable-tpu |
                         --enable-vertical-pod-autoscaling | --help |
                         --image-type | --issue-client-certificate | --labels |
                         --local-ssd-count | --machine-type |
                         --maintenance-window | --maintenance-window-end |
                         --maintenance-window-recurrence |
                         --maintenance-window-start |
                         --master-authorized-networks | --master-ipv4-cidr |
                         --max-accelerator | --max-cpu | --max-memory |
                         --max-nodes | --max-nodes-per-pool |
                         --max-pods-per-node | --metadata |
                         --metadata-from-file | --min-accelerator | --min-cpu |
                         --min-cpu-platform | --min-memory | --min-nodes |
                         --network | --node-labels | --node-locations |
                         --node-taints | --node-version | --num-nodes |
                         --password | --preemptible | --region | --reservation |
                         --reservation-affinity |
                         --resource-usage-bigquery-dataset | --scopes |
                         --service-account | --services-ipv4-cidr |
                         --services-secondary-range-name |
                         --shielded-integrity-monitoring |
                         --shielded-secure-boot | --subnetwork | --tags |
                         --tpu-ipv4-cidr | --username | --zone
```

#### List GKE Clusters
```
$ gcloud container clusters list

NAME               LOCATION       MASTER_VERSION  MASTER_IP       MACHINE_TYPE   NODE_VERSION    NUM_NODES  STATUS
spring-gs-cluster  asia-northeast1-b  1.13.11-gke.14  ...........
```

```
$ kubectl config get-contexts

CURRENT   NAME                                                 CLUSTER                                              AUTHINFO                                             NAMESPACE
*         gke_.........
```

#### Delete GKE Cluster
`Usage: gcloud container clusters delete NAME [optional flags]`

```
$ gcloud container clusters delete spring-gs-cluster \
  --zone asia-northeast1-b
```

### Backend Deployment
#### Redis
- [Deployment YAML](yaml/redis-deployment.yml)

```
$ kubectl apply -f redis-deployment.yml
```

```
$ kubectl get pods -o wide
$ gcloud compute ssh <NODE_NAME>
```

- [Service YAML](yaml/redis-service.yml)

```
$ kubectl apply -f redis-service.yml --record
$ kubectl get services -o wide
```

#### MySQL
#### 1. Secret: MySQL Credential Data
- [Secret YAML](yaml/mysql-secret.yml)

```yaml
metadata:
  name: mysql-secret
```

|Environment Variables|Contents|Type|
|---------------------|--------|----|
|MYSQL_ROOT_PASSWORD|It specifies the password that will be set for the MySQL root superuser account|Mandatory|
|MYSQL_DATABASE|It allows you to specify the name of a database to be created on image startup|Optional|
|MYSQL_USER|It creates a new user and to set that user's password|Optional|
|MYSQL_PASSWORD|It creates a new user's password|Optional|

- BASE64 Encoded
```
$ echo -n "<TEXT>" | openssl enc -e -base64
```

#### 2. ConfigMap: MySQL Configuration
- [ConfigMap YAML](yaml/mysql-configmap.yml)

```yaml
metadata:
  name: mysql-config-file
```

Above MySQL8 has default authentication `caching_sha2_password` though, we'll use **`mysql_native_password`** as so far.

|mysqld||
|------|-|
|default_authentication_plugin|mysql_native_password|

#### 3. StatefulSet: MySQL Deployment
- [StatefulSet YAML](yaml/mysql-statefulset.yml)

```yaml
envFrom:
- configMapRef:
    name: mysql-config-file
- secretRef:
    name: mysql-secret
```

#### 4. Service: MySQL Headless Service
- [Service YAML](yaml/mysql-service.yml)

```yaml
clusterIP: None
```

```
$ kubectl run alpine -it --rm --image alpine --generator=run-pod/v1 -- ash

If you don't see a command prompt, try pressing enter.
/ # nslookup mysql
Server:		10.7.240.10
Address:	10.7.240.10:53

Name:	mysql.default.svc.cluster.local
Address: 10.4.2.3
```

##### 4.1. Endpoint

```
$ kubectl get endpoints
```

##### 4.2. Persistent Volume

```
$ kubectl get pv -o wide

NAME                                       CAPACITY   ACCESS MODES   RECLAIM POLICY   STATUS   CLAIM                                      STORAGECLASS   REASON   AGE
pvc-741994fe-53a6-11ea-a350-42010a92009d   20Gi       RWO            Delete           Bound    default/mysql-persistent-storage-mysql-0   standard                169m
```

##### 4.3. Persistent Volume Claim

```
$ kubectl get pvc -o wide

NAME                               STATUS   VOLUME                                     CAPACITY   ACCESS MODES   STORAGECLASS   AGE
mysql-persistent-storage-mysql-0   Bound    pvc-741994fe-53a6-11ea-a350-42010a92009d   20Gi       RWO            standard       174m
```

##### 4.4. Access MySQL on K8s

```
$ kubectl exec -it pod/mysql-0 -- mysql -uguest -pguest --database=app
```

#### Zipkin

- [Deployment YAML](yaml/zipkin-deployment.yml)

```yaml
image: gcr.io/stackdriver-trace-docker/zipkin-collector:v0.6.0
```

```
$ kubectl apply -f zipkin-deployment.yml
```

```
$ kubectl get pods -o wide
```

- [Service YAML](yaml/zipkin-service.yml)

```
$ kubectl apply -f zipkin-service.yml --record
$ kubectl get services -o wide
```

### Spring App - Hello World
- [spring-hello-app](spring-hello-app)

```
$ kubectl apply -f deployment.yml
```

### Spring App - Database Integration
- [guestbook](guestbook)

#### 1. MySQL 8.X

- Below MySQL 8
  - **Driver**: `com.mysql.jdbc.Driver`
  - **Hibernate**: `org.hibernate.dialect.MySQLDialect`
- Above MySQL 8
  - **Driver**: `com.mysql.cj.jdbc.Driver`
  - **Hibernate**: `org.hibernate.dialect.MySQL8Dialect`

```yaml
spring:
  datasource:
    driverClassName: com.mysql.cj.jdbc.Driver
  jpa:
    database-platform: org.hibernate.dialect.MySQL8Dialect
```



#### 2. Spring Data JPA
```gradle
plugins {
	kotlin("plugin.jpa") version "1.3.61"
	kotlin("plugin.noarg") version "1.3.61"
}
dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-data-rest")
}
```

#### 3. Entity
- `Kotolin Data Class`

```kotlin
@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
data class Message
```

##### ID
```kotlin
@Id
@GeneratedValue(generator = "uuid2")
@GenericGenerator(name = "uuid2", strategy = "uuid2")
@Column(columnDefinition = "varchar(36)")
val id: String 
```

#### Repository
```kotlin
@RepositoryRestResource
interface MessageRepository : PagingAndSortingRepository<Message, Long>
```

#### Initialize Database
- schema.sql
- data.sql

```dockerfile
FROM gradle:6.0.1-jdk11 as compile
COPY . /home/source/java
WORKDIR /home/source/java/guestbook
USER root
RUN chown -R gradle /home/source/java/guestbook
USER gradle
RUN ls -l && gradle clean assemble

FROM openjdk:8 as cdbg
RUN mkdir /opt/cdbg
RUN curl https://storage.googleapis.com/cloud-debugger/compute-java/debian-wheezy/cdbg_java_agent_gce.tar.gz | tar zxv -C /opt/cdbg

FROM openjdk:11.0.5-jre-slim-buster
WORKDIR /home/application/java
RUN mkdir /opt/cdbg
COPY --from=cdbg "/opt/cdbg/*" /opt/cdbg/
COPY --from=compile "/home/source/java/guestbook/build/libs/guestbook-0.0.1-SNAPSHOT.jar" ./app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-agentpath:/opt/cdbg/cdbg_java_agent.so", "-Dcom.google.cdbg.module=guestbook-service", "-Dcom.google.cdbg.version=0.0.1-SNAPSHOT","-jar", "app.jar"]
```

##### StackDriver
|Parameter|Context|
|---------|-------|
|com.google.cdbg.module|Application Name|
|com.google.cdbg.version|Application Version|


### Spring App - Deployment and Service YAML

### Client on Kubernetes

```
$ kubectl run alpine -it --rm --image alpine --generator=run-pod/v1 -- ash
$ apk add --update curl
```

## Features

- feature:1
- feature:2

## Requirement

## Usage

### YAML for Kubernetes

#### Mapping between Resource and API-Group
<details>
<summary>kubectl api-resources</summary>

```
$ kubectl api-resources

NAME                              SHORTNAMES   APIGROUP                       NAMESPACED   KIND
bindings                                                                      true         Binding
componentstatuses                 cs                                          false        ComponentStatus
configmaps                        cm                                          true         ConfigMap
endpoints                         ep                                          true         Endpoints
events                            ev                                          true         Event
limitranges                       limits                                      true         LimitRange
namespaces                        ns                                          false        Namespace
nodes                             no                                          false        Node
persistentvolumeclaims            pvc                                         true         PersistentVolumeClaim
persistentvolumes                 pv                                          false        PersistentVolume
pods                              po                                          true         Pod
podtemplates                                                                  true         PodTemplate
replicationcontrollers            rc                                          true         ReplicationController
resourcequotas                    quota                                       true         ResourceQuota
secrets                                                                       true         Secret
serviceaccounts                   sa                                          true         ServiceAccount
services                          svc                                         true         Service
mutatingwebhookconfigurations                  admissionregistration.k8s.io   false        MutatingWebhookConfiguration
validatingwebhookconfigurations                admissionregistration.k8s.io   false        ValidatingWebhookConfiguration
customresourcedefinitions         crd,crds     apiextensions.k8s.io           false        CustomResourceDefinition
apiservices                                    apiregistration.k8s.io         false        APIService
controllerrevisions                            apps                           true         ControllerRevision
daemonsets                        ds           apps                           true         DaemonSet
deployments                       deploy       apps                           true         Deployment
replicasets                       rs           apps                           true         ReplicaSet
statefulsets                      sts          apps                           true         StatefulSet
tokenreviews                                   authentication.k8s.io          false        TokenReview
localsubjectaccessreviews                      authorization.k8s.io           true         LocalSubjectAccessReview
selfsubjectaccessreviews                       authorization.k8s.io           false        SelfSubjectAccessReview
selfsubjectrulesreviews                        authorization.k8s.io           false        SelfSubjectRulesReview
subjectaccessreviews                           authorization.k8s.io           false        SubjectAccessReview
horizontalpodautoscalers          hpa          autoscaling                    true         HorizontalPodAutoscaler
cronjobs                          cj           batch                          true         CronJob
jobs                                           batch                          true         Job
certificatesigningrequests        csr          certificates.k8s.io            false        CertificateSigningRequest
backendconfigs                                 cloud.google.com               true         BackendConfig
leases                                         coordination.k8s.io            true         Lease
daemonsets                        ds           extensions                     true         DaemonSet
deployments                       deploy       extensions                     true         Deployment
ingresses                         ing          extensions                     true         Ingress
networkpolicies                   netpol       extensions                     true         NetworkPolicy
podsecuritypolicies               psp          extensions                     false        PodSecurityPolicy
replicasets                       rs           extensions                     true         ReplicaSet
nodes                                          metrics.k8s.io                 false        NodeMetrics
pods                                           metrics.k8s.io                 true         PodMetrics
managedcertificates               mcrt         networking.gke.io              true         ManagedCertificate
networkpolicies                   netpol       networking.k8s.io              true         NetworkPolicy
updateinfos                       updinf       nodemanagement.gke.io          true         UpdateInfo
poddisruptionbudgets              pdb          policy                         true         PodDisruptionBudget
podsecuritypolicies               psp          policy                         false        PodSecurityPolicy
clusterrolebindings                            rbac.authorization.k8s.io      false        ClusterRoleBinding
clusterroles                                   rbac.authorization.k8s.io      false        ClusterRole
rolebindings                                   rbac.authorization.k8s.io      true         RoleBinding
roles                                          rbac.authorization.k8s.io      true         Role
scalingpolicies                                scalingpolicy.kope.io          true         ScalingPolicy
priorityclasses                   pc           scheduling.k8s.io              false        PriorityClass
storageclasses                    sc           storage.k8s.io                 false        StorageClass
volumeattachments                              storage.k8s.io                 false        VolumeAttachment
```

</details>

#### API-Group version
<details>
<summary>kubectl api-versions</summary>

```
$ kubectl api-versions

admissionregistration.k8s.io/v1beta1
apiextensions.k8s.io/v1beta1
apiregistration.k8s.io/v1
apiregistration.k8s.io/v1beta1
apps/v1
apps/v1beta1
apps/v1beta2
authentication.k8s.io/v1
authentication.k8s.io/v1beta1
authorization.k8s.io/v1
authorization.k8s.io/v1beta1
autoscaling/v1
autoscaling/v2beta1
batch/v1
batch/v1beta1
certificates.k8s.io/v1beta1
cloud.google.com/v1beta1
coordination.k8s.io/v1beta1
extensions/v1beta1
metrics.k8s.io/v1beta1
networking.gke.io/v1beta1
networking.k8s.io/v1
nodemanagement.gke.io/v1alpha1
policy/v1beta1
rbac.authorization.k8s.io/v1
rbac.authorization.k8s.io/v1beta1
scalingpolicy.kope.io/v1alpha1
scheduling.k8s.io/v1beta1
storage.k8s.io/v1
storage.k8s.io/v1beta1
v1
```

</details>

## Installation

## Licence

Released under the [MIT license](https://gist.githubusercontent.com/shinyay/56e54ee4c0e22db8211e05e70a63247e/raw/34c6fdd50d54aa8e23560c296424aeb61599aa71/LICENSE)

## Author

[shinyay](https://github.com/shinyay)
