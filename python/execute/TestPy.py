import matplotlib.pyplot as plt
import pandas as pd
import matplotlib.font_manager as fm

# Sample DataFrames from the provided data
data_components = {
    'Lot 번호': ['LOT20230601', 'LOT20230601', 'LOT20230601', 'LOT20230602', 'LOT20230602', 'LOT20230602',
                 'LOT20230603', 'LOT20230603', 'LOT20230603', 'LOT20230604', 'LOT20230604', 'LOT20230604',
                 'LOT20230605', 'LOT20230605', 'LOT20230605', 'LOT20230606', 'LOT20230606', 'LOT20230606'],
    '구성품 코드': ['C001', 'C002', 'C003', 'C001', 'C002', 'C003', 'C001', 'C002', 'C003', 'C001', 'C002', 'C003',
                'C001', 'C002', 'C003', 'C001', 'C002', 'C003'],
    '구성품 Lot 번호': ['CLOT20230501', 'CLOT20230502', 'CLOT20230503', 'CLOT20230504', 'CLOT20230505', 'CLOT20230506',
                   'CLOT20230507', 'CLOT20230508', 'CLOT20230509', 'CLOT20230510', 'CLOT20230511', 'CLOT20230512',
                   'CLOT20230513', 'CLOT20230514', 'CLOT20230515', 'CLOT20230516', 'CLOT20230517', 'CLOT20230518'],
    '수량': [100, 200, 300, 150, 250, 350, 100, 200, 300, 150, 250, 350, 100, 200, 300, 150, 250, 350]
}

data_products = {
    'Lot 번호': ['LOT20230601', 'LOT20230602', 'LOT20230603', 'LOT20230604', 'LOT20230605', 'LOT20230606'],
    '제품 코드': ['P001', 'P002', 'P003', 'P004', 'P005', 'P006'],
    '생산 일자': ['2023-06-01', '2023-06-02', '2023-06-03', '2023-06-04', '2023-06-05', '2023-06-06'],
    '유효 기간': ['2024-06-01', '2024-06-02', '2024-06-03', '2024-06-04', '2024-06-05', '2024-06-06'],
    '생산 라인': ['Line 1', 'Line 2', 'Line 1', 'Line 3', 'Line 2', 'Line 1'],
    '창고 위치': ['WH-A', 'WH-B', 'WH-C', 'WH-D', 'WH-E', 'WH-A'],
    '입고 일자': ['2023-06-02', '2023-06-03', '2023-06-04', '2023-06-05', '2023-06-06', '2023-06-07'],
    '출고 일자': ['2023-06-10', '2023-06-12', '2023-06-15', '2023-06-20', '2023-06-22', '2023-06-25'],
    '수량': [500, 1000, 200, 300, 150, 600],
    '상태': ['출고 완료', '출고 완료', '출고 완료', '출고 완료', '출고 완료', '출고 완료']
}

df_components = pd.DataFrame(data_components)
df_products = pd.DataFrame(data_products)

# Merging the two dataframes on 'Lot 번호'
merged_df = pd.merge(df_components, df_products, on='Lot 번호')

# Plotting
fig, ax1 = plt.subplots(figsize=(14, 8))

# Bar plot for components quantities
components_sum = df_components.groupby('Lot 번호')['수량'].sum()
components_sum.plot(kind='bar', color='skyblue', ax=ax1, position=0, width=0.4, label='Components Quantity')

# Line plot for products quantities
products_sum = df_products.set_index('Lot 번호')['수량']
products_sum.plot(kind='line', marker='o', ax=ax1, color='orange', label='Products Quantity', secondary_y=False)

ax1.set_xlabel('Lot 번호')
ax1.set_ylabel('Components Quantity', color='skyblue')
ax1.set_title('Components and Products Quantity by Lot Number')
ax1.legend(loc='upper left')

# Aligning and displaying plot
plt.tight_layout()

#plt.show()
## save image
image_path = '../components_products_quantity_by_lot.png'
plt.savefig(image_path)


# import ace_tools as tools; tools.display_dataframe_to_user(name="Merged DataFrame", dataframe=merged_df)

# Creating .pythonrc.py content
pythonrc_content = """
import matplotlib
from matplotlib import rc

# Use specific font for Korean
font_path = 'YOUR_FONT_PATH_HERE.ttf'
font_name = matplotlib.font_manager.FontProperties(fname=font_path).get_name()
rc('font', family=font_name)

# Setting default for axes unicode minus to False
matplotlib.rcParams['axes.unicode_minus'] = False
"""

# Save to .pythonrc.py
"""
with open('/mnt/data/.pythonrc.py', 'w') as f:
    f.write(pythonrc_content)

('/mnt/data/.pythonrc.py', merged_df)
"""