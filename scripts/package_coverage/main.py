import os

from config import *
from utils import get_package_list, parse_imports, get_class_list, parse_tokens

all_packages_set = set(get_package_list())
used_packages_set = set()

all_classes_set = set(get_class_list())
used_classes_set = set()

for (dirpath, dirnames, filenames) in os.walk(ANDROID_SRC_DIR):
    for filename in filenames:
        if filename.endswith(".java"):
            used_packages_set.update(parse_imports(os.path.join(dirpath, filename)))
            token_list = parse_tokens(os.path.join(dirpath, filename))
            for token in token_list:
                if (token in all_classes_set):
                    used_classes_set.add(token)                

packages_cross_set = all_packages_set & used_packages_set
packages_cross_list = sorted(list(packages_cross_set))

used_classes_list = sorted(list(used_classes_set))

package_used_percentage = 1.0 * len(packages_cross_set)/len(all_packages_set)
print("Used percentage: {0}%".format(package_used_percentage * 100))

class_used_percentage = 1.0 * len(used_classes_set) / len(all_classes_set)
print("Used percentage: {0}%".format(class_used_percentage * 100))

with open(USED_PACKAGES_FILE, "w") as f:
    for package in packages_cross_list:
        f.write("{0}\n".format(package))

with open(USED_CLASSES_FILE, "w") as f:
    for each in used_classes_list:
        f.write("{0}\n".format(each))
