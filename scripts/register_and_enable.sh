BASEDIR=$(dirname "$0")
# echo Please make sure you have run ./gradlew clean generateDescriptors before starting this script
pushd "$BASEDIR/../service"

# Check for decriptor target directory.

DESCRIPTORDIR="build/resources/main/okapi"

if [ ! -d "$DESCRIPTORDIR" ]; then
    echo "No descriptors found. Let's try building them."
    ./gradlew generateDescriptors
fi

curl -XDELETE http://localhost:9130/_/proxy/tenants/diku/modules/mod-licenses-1.1.0
curl -XDELETE http://localhost:9130/_/discovery/modules/mod-licenses-1.1.0/localhost-mod-licenses-1.0.0
curl -XDELETE http://localhost:9130/_/proxy/modules/mod-licenses-1.1.0
# ./gradlew clean generateDescriptors
curl -XPOST http://localhost:9130/_/proxy/modules -d @"${DESCRIPTORDIR}/ModuleDescriptor.json"
curl -XPOST http://localhost:9130/_/discovery/modules -d @"${DESCRIPTORDIR}/DeploymentDescriptor.json"
curl -XPOST http://localhost:9130/_/proxy/tenants/diku/modules -d '{"id": "mod-licenses-1.1.0"}'
popd
