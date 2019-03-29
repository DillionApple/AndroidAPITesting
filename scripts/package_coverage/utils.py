import os
import re

PACKAGES_FILE = os.path.join(os.path.dirname(os.path.realpath(__file__)), "packages.txt");

def get_package_list():
    with open(PACKAGES_FILE, "r") as f:
        ret = f.read().strip().split("\n")
    return ret

def parse_imports(file_path):
    ret = []
    pattern = "import (.*?);"
    with open(file_path, "r") as f:
        text = f.read()
        import_list = re.findall(pattern, text)
    for each_import in import_list:
        ret.append(each_import)
        for i in range(len(each_import)):
            if (each_import[i] == "."):
                ret.append(each_import[0:i])
                    
    return ret
