# Stox
<p align="center">
  <img src="https://github.com/eltonkola/Stox/blob/main/images/play_store_512.png" width="200">
</p>
StoX is a simple stocks tracker app that uses the Polygon API to fetch stock data.


##Resources
Icon generated with leonardo app (https://leonardo.app/), adapted with icon.kitchen(https://icon.kitchen/)
Theme generated with material theme builder (https://material-foundation.github.io/material-theme-builder/)

## Screenshots

|                                                                                             |                                                                                             |                                                                                             |                                                                                             |                                                                                             |
| :-------------------------------------------------------------------------------------------: | :-------------------------------------------------------------------------------------------: | :-------------------------------------------------------------------------------------------: | :-------------------------------------------------------------------------------------------: | :-------------------------------------------------------------------------------------------: |
| <img src="https://github.com/eltonkola/Stox/blob/main/images/screenshot_1.png" width="200"> | <img src="https://github.com/eltonkola/Stox/blob/main/images/screenshot_2.png" width="200"> | <img src="https://github.com/eltonkola/Stox/blob/main/images/screenshot_3.png" width="200"> | <img src="https://github.com/eltonkola/Stox/blob/main/images/screenshot_4.png" width="200"> | <img src="https://github.com/eltonkola/Stox/blob/main/images/screenshot_5.png" width="200"> |



##Architecture and Dependency graph
![dependency graph](https://github.com/eltonkola/Stox/blob/main/images/module_graph.svg)

###Core data
This module contains the data layer of the app. It contains the repository, local and remote data sources.

###Core domain
This module contains the domain layer of the app. It contains the use cases and the models.

###Core UI
It contains the components and the themes.

###Feature modules
Each feature module contains the screens and the view models.


'''mermaid
graph TD
    app[":app"]
    core_ui[":core-ui"]
    core_domain[":core-domain"]
    core_data[":core-data"]
    feature_stocks_overview[":feature-stocks-overview"]
    feature_stock_list[":feature-stock-list"]
    feature_stock_detail[":feature-stock-detail"]
    feature_add_stock[":feature-add-stock"]

    app --> feature_stocks_overview
    app --> feature_stock_list
    app --> feature_stock_detail
    app --> feature_add_stock
    
    core_domain --> core_data
    
    feature_stocks_overview --> core_ui
    feature_stocks_overview --> core_domain
    
    feature_stock_list --> core_ui
    feature_stock_list --> core_domain
    
    feature_stock_detail --> core_ui
    feature_stock_detail --> core_domain
    
    feature_add_stock --> core_ui
    feature_add_stock --> core_domain
'''
