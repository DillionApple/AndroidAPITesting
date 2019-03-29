import os

from config import *
from utils import get_package_list, parse_imports

all_packages_set = set(get_package_list())
used_packages_set = set()

for (dirpath, dirnames, filenames) in os.walk(ANDROID_SRC_DIR):
    for filename in filenames:
        if filename.endswith(".java"):
            used_packages_set.update(parse_imports(os.path.join(dirpath, filename)))

cross_set = all_packages_set & used_packages_set

print("Used Android Packages: {0}".format(cross_set))
used_percentage = 1.0 * len(cross_set)/len(all_packages_set)
print("Used percentage: {0}%".format(used_percentage * 100))
