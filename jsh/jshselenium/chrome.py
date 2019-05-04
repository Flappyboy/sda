from selenium import webdriver
from selenium.webdriver.support.wait import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.common.by import By

driver = webdriver.Chrome()

driver.get("http://www.baidu.com")
WebDriverWait(driver,20,0.5).until(
    EC.presence_of_element_located((By.LINK_TEXT, 'CSDN')))

print (driver.find_element_by_link_text('CSDN').get_attribute('href'))

print(driver.page_source)


# browser.close()